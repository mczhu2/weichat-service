package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.callback.ReplyMediaItem;
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
     * Upload an image by remote URL.
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
     * Upload an image by base64 payload.
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
     * Upload a normalized reply image.
     */
    public CdnUploadResponse uploadImage(String uuid, ReplyMediaItem imagePayload) {
        if (imagePayload == null) {
            throw new IllegalArgumentException("Reply image payload is empty");
        }
        if (StringUtils.hasText(imagePayload.getUrl())) {
            return uploadImageByUrl(uuid, imagePayload.getUrl());
        }
        if (StringUtils.hasText(imagePayload.getBase64())) {
            return uploadImageByBase64(
                    uuid,
                    imagePayload.getBase64(),
                    imagePayload.getFilename(),
                    imagePayload.getContentType()
            );
        }
        throw new IllegalArgumentException("Reply image must contain url or base64 data");
    }

    /**
     * Extract the upload response body and convert API errors to exceptions.
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
     * Support both data:image/...;base64 and raw base64 payloads.
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
     * Build a fallback filename from MIME type.
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
