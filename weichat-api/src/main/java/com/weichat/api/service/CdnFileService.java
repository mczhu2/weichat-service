package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.cdn.CdnUploadFileRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.UUID;

@Service
public class CdnFileService {

    @Autowired
    private WxWorkApiClient client;

    /**
     * 通过网络文件地址上传到 CDN，语音文件可复用这条链路。
     */
    public CdnUploadResponse uploadFileByUrl(String uuid, String fileUrl, String fileName) {
        CdnUploadFileRequest request = CdnUploadFileRequest.builder()
                .uuid(uuid)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .build();
        JSONObject response = client.post("/wxwork/UploadCdnLinkFile", (JSONObject) JSON.toJSON(request));
        return extractUploadResponse(response, "url");
    }

    /**
     * 通过 base64 文件内容上传到 CDN，适合语音等二进制媒体回复。
     */
    public CdnUploadResponse uploadFileByBase64(String uuid,
                                                String base64Payload,
                                                String filename,
                                                String contentType) {
        Base64FileData fileData = decodeBase64Payload(base64Payload, filename, contentType);
        JSONObject response = client.postMultipart(
                "/wxwork/CdnUploadFile",
                uuid,
                "file",
                fileData.getFilename(),
                fileData.getContentType(),
                fileData.getBytes()
        );
        return extractUploadResponse(response, "base64");
    }

    /**
     * 兼容语音/文件回复的 URL 和 base64 两种载荷格式。
     */
    public CdnUploadResponse uploadFile(String uuid, JSONObject filePayload) {
        if (filePayload == null) {
            throw new IllegalArgumentException("Reply file payload is empty");
        }
        String fileUrl = firstNonBlank(
                filePayload.getString("url"),
                filePayload.getString("fileUrl"),
                filePayload.getString("voiceUrl"),
                filePayload.getString("audioUrl")
        );
        if (StringUtils.hasText(fileUrl)) {
            return uploadFileByUrl(
                    uuid,
                    fileUrl,
                    firstNonBlank(filePayload.getString("filename"), filePayload.getString("fileName"))
            );
        }

        String base64Payload = firstNonBlank(
                filePayload.getString("base64"),
                filePayload.getString("data"),
                filePayload.getString("content")
        );
        if (StringUtils.hasText(base64Payload)) {
            return uploadFileByBase64(
                    uuid,
                    base64Payload,
                    firstNonBlank(filePayload.getString("filename"), filePayload.getString("fileName")),
                    firstNonBlank(filePayload.getString("contentType"), filePayload.getString("mimeType"))
            );
        }

        throw new IllegalArgumentException("Reply file must contain url or base64 data");
    }

    /**
     * 上传成功后统一抽取 data，并把下游错误转成明确异常。
     */
    private CdnUploadResponse extractUploadResponse(JSONObject response, String sourceType) {
        ApiResult<CdnUploadResponse> result = ApiResult.from(response, CdnUploadResponse.class);
        if (result == null) {
            throw new IllegalStateException("CDN file upload returned null result for " + sourceType);
        }
        if (result.getCode() != 0) {
            throw new IllegalStateException("CDN file upload failed for " + sourceType + ": " + result.getMsg());
        }
        if (result.getData() == null) {
            throw new IllegalStateException("CDN file upload returned empty data for " + sourceType);
        }
        return result.getData();
    }

    /**
     * 兼容 data:...;base64 前缀和裸 base64 两种格式。
     */
    private Base64FileData decodeBase64Payload(String base64Payload, String filename, String contentType) {
        String trimmed = base64Payload == null ? "" : base64Payload.trim();
        String resolvedContentType = contentType;
        String encoded = trimmed;

        int commaIndex = trimmed.indexOf(',');
        if (trimmed.startsWith("data:") && commaIndex > 0) {
            String meta = trimmed.substring(5, commaIndex);
            encoded = trimmed.substring(commaIndex + 1);
            if (!StringUtils.hasText(resolvedContentType)) {
                int semicolonIndex = meta.indexOf(';');
                resolvedContentType = semicolonIndex >= 0 ? meta.substring(0, semicolonIndex) : meta;
            }
        }

        byte[] bytes = Base64.getDecoder().decode(encoded.replaceAll("\\s", ""));
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Base64 file is empty");
        }

        String finalContentType = StringUtils.hasText(resolvedContentType)
                ? resolvedContentType
                : "application/octet-stream";
        String finalFilename = StringUtils.hasText(filename)
                ? filename
                : buildFilename(finalContentType);
        return new Base64FileData(bytes, finalFilename, finalContentType);
    }

    /**
     * 当调用方未提供文件名时，按 MIME 类型生成默认扩展名。
     */
    private String buildFilename(String contentType) {
        String extension = "bin";
        if (StringUtils.hasText(contentType)) {
            int slashIndex = contentType.indexOf('/');
            if (slashIndex >= 0 && slashIndex < contentType.length() - 1) {
                extension = contentType.substring(slashIndex + 1).replaceAll("[^A-Za-z0-9]", "");
            }
        }
        if (!StringUtils.hasText(extension)) {
            extension = "bin";
        }
        return "reply-" + UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }

    /**
     * 从多个候选字段里取第一个非空值，减少不同回调协议字段名差异带来的判断分支。
     */
    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private static class Base64FileData {
        private final byte[] bytes;
        private final String filename;
        private final String contentType;

        private Base64FileData(byte[] bytes, String filename, String contentType) {
            this.bytes = bytes;
            this.filename = filename;
            this.contentType = contentType;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String getFilename() {
            return filename;
        }

        public String getContentType() {
            return contentType;
        }
    }
}
