package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.cdn.CdnUploadImgRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.UUID;

@Service
public class CdnImageService {

    @Autowired
    private WxWorkApiClient client;

    /**
     * 通过网络图片地址上传到 CDN，适合外部可访问的图片 URL。
     */
    public CdnUploadResponse uploadImageByUrl(String uuid, String imageUrl) {
        CdnUploadImgRequest request = CdnUploadImgRequest.builder()
                .uuid(uuid)
                .url(imageUrl)
                .build();
        JSONObject response = client.post("/wxwork/CdnUploadImgLink", (JSONObject) JSON.toJSON(request));
        return extractUploadResponse(response, "url");
    }

    /**
     * 通过 base64 图片内容上传到 CDN，内部会先解码再按 multipart 方式上传。
     */
    public CdnUploadResponse uploadImageByBase64(String uuid,
                                                 String base64Payload,
                                                 String filename,
                                                 String contentType) {
        Base64ImageData imageData = decodeBase64Payload(base64Payload, filename, contentType);
        JSONObject response = client.postMultipart(
                "/wxwork/CdnUploadImg",
                uuid,
                "file",
                imageData.getFilename(),
                imageData.getContentType(),
                imageData.getBytes()
        );
        return extractUploadResponse(response, "base64");
    }

    /**
     * 兼容回复图片的多种载荷格式，优先识别 URL，其次识别 base64。
     */
    public CdnUploadResponse uploadImage(String uuid, JSONObject imagePayload) {
        if (imagePayload == null) {
            throw new IllegalArgumentException("Reply image payload is empty");
        }
        String imageUrl = firstNonBlank(
                imagePayload.getString("url"),
                imagePayload.getString("imageUrl"),
                imagePayload.getString("image_url")
        );
        if (StringUtils.hasText(imageUrl)) {
            return uploadImageByUrl(uuid, imageUrl);
        }

        String base64Payload = firstNonBlank(
                imagePayload.getString("base64"),
                imagePayload.getString("data"),
                imagePayload.getString("content")
        );
        if (StringUtils.hasText(base64Payload)) {
            return uploadImageByBase64(
                    uuid,
                    base64Payload,
                    firstNonBlank(imagePayload.getString("filename"), imagePayload.getString("fileName")),
                    firstNonBlank(imagePayload.getString("contentType"), imagePayload.getString("mimeType"))
            );
        }

        throw new IllegalArgumentException("Reply image must contain url or base64 data");
    }

    /**
     * 上传成功后统一抽取 data，并把下游错误转成明确异常。
     */
    private CdnUploadResponse extractUploadResponse(JSONObject response, String sourceType) {
        ApiResult<CdnUploadResponse> result = ApiResult.from(response, CdnUploadResponse.class);
        if (result == null) {
            throw new IllegalStateException("CDN upload returned null result for " + sourceType);
        }
        if (result.getCode() != 0) {
            throw new IllegalStateException("CDN upload failed for " + sourceType + ": " + result.getMsg());
        }
        if (result.getData() == null) {
            throw new IllegalStateException("CDN upload returned empty data for " + sourceType);
        }
        return result.getData();
    }

    /**
     * 兼容 data:image/...;base64 前缀和裸 base64 两种格式。
     */
    private Base64ImageData decodeBase64Payload(String base64Payload, String filename, String contentType) {
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
            throw new IllegalArgumentException("Base64 image is empty");
        }

        String finalContentType = StringUtils.hasText(resolvedContentType)
                ? resolvedContentType
                : "image/png";
        String finalFilename = StringUtils.hasText(filename)
                ? filename
                : buildFilename(finalContentType);
        return new Base64ImageData(bytes, finalFilename, finalContentType);
    }

    /**
     * 当调用方未提供文件名时，根据 MIME 类型生成一个可上传的默认文件名。
     */
    private String buildFilename(String contentType) {
        String extension = "png";
        if (StringUtils.hasText(contentType)) {
            int slashIndex = contentType.indexOf('/');
            if (slashIndex >= 0 && slashIndex < contentType.length() - 1) {
                extension = contentType.substring(slashIndex + 1).replaceAll("[^A-Za-z0-9]", "");
            }
        }
        if (!StringUtils.hasText(extension)) {
            extension = "png";
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

    private static class Base64ImageData {
        private final byte[] bytes;
        private final String filename;
        private final String contentType;

        private Base64ImageData(byte[] bytes, String filename, String contentType) {
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
