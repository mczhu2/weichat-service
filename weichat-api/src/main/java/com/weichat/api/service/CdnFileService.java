package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.RemoteMediaDownloadService.RemoteMediaResource;
import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

@Service
public class CdnFileService {

    @Autowired
    private WxWorkApiClient client;

    @Autowired
    private RemoteMediaDownloadService remoteMediaDownloadService;

    /**
     * Upload a file by remote URL.
     */
    public CdnUploadResponse uploadFileByUrl(String uuid, String fileUrl, String fileName) {
        return uploadFileByUrl(uuid, fileUrl, fileName, null);
    }

    /**
     * Upload a file by remote URL.
     */
    public CdnUploadResponse uploadVideoByUrl(String uuid, String fileUrl, String fileName) {
        return uploadVideoFileByUrl(uuid, fileUrl, fileName, null);
    }

    /**
     * Upload a file by remote URL with preferred content type metadata.
     */
    public CdnUploadResponse uploadFileByUrl(String uuid,
                                             String fileUrl,
                                             String fileName,
                                             String contentType) {
        RemoteMediaResource fileResource = remoteMediaDownloadService.download(
                fileUrl,
                fileName,
                StringUtils.hasText(contentType) ? contentType : "application/octet-stream",
                "file"
        );
        JSONObject response = client.postMultipart(
                "/wxwork/CdnUploadFile",
                uuid,
                "file",
                fileResource.getFilename(),
                fileResource.getContentType(),
                fileResource.getBytes()
        );
        return extractUploadResponse(response, "url");
    }

    /**
     * Upload a file by remote URL with preferred content type metadata.
     */
    public CdnUploadResponse uploadVideoFileByUrl(String uuid,
                                             String fileUrl,
                                             String fileName,
                                             String contentType) {
        RemoteMediaResource fileResource = remoteMediaDownloadService.download(
                fileUrl,
                fileName,
                StringUtils.hasText(contentType) ? contentType : "application/octet-stream",
                "file"
        );
        JSONObject response = client.postMultipart(
                "/wxwork/CdnUploadVideo",
                uuid,
                "file",
                fileResource.getFilename(),
                fileResource.getContentType(),
                fileResource.getBytes()
        );
        return extractUploadResponse(response, "url");
    }

    /**
     * Upload a file by remote URL with preferred content type metadata.
     */
    public CdnUploadResponse uploadImageByUrl(String uuid,
                                             String fileUrl,
                                             String fileName,
                                             String contentType) {
        RemoteMediaResource fileResource = remoteMediaDownloadService.download(
                fileUrl,
                fileName,
                StringUtils.hasText(contentType) ? contentType : "application/octet-stream",
                "file"
        );
        JSONObject response = client.postMultipart(
                "/wxwork/CdnUploadImg",
                uuid,
                "file",
                fileResource.getFilename(),
                fileResource.getContentType(),
                fileResource.getBytes()
        );
        return extractUploadResponse(response, "url");
    }

    /**
     * Upload a file by base64 payload.
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
     * Upload a normalized reply file or voice payload.
     */
    public CdnUploadResponse uploadFile(String uuid, ReplyMediaItem filePayload) {
        if (filePayload == null) {
            throw new IllegalArgumentException("Reply file payload is empty");
        }
        if (StringUtils.hasText(filePayload.getUrl())) {
            return uploadFileByUrl(
                    uuid,
                    filePayload.getUrl(),
                    filePayload.getFilename(),
                    filePayload.getContentType()
            );
        }
        if (StringUtils.hasText(filePayload.getBase64())) {
            return uploadFileByBase64(
                    uuid,
                    filePayload.getBase64(),
                    filePayload.getFilename(),
                    filePayload.getContentType()
            );
        }
        throw new IllegalArgumentException("Reply file must contain url or base64 data");
    }

    /**
     * Upload a normalized reply file or voice payload.
     */
    public CdnUploadResponse uploadVideoFile(String uuid, ReplyMediaItem filePayload) {
        if (filePayload == null) {
            throw new IllegalArgumentException("Reply file payload is empty");
        }
        if (StringUtils.hasText(filePayload.getUrl())) {
            return uploadVideoFileByUrl(
                    uuid,
                    filePayload.getUrl(),
                    filePayload.getFilename(),
                    filePayload.getContentType()
            );
        }
        if (StringUtils.hasText(filePayload.getBase64())) {
            return uploadFileByBase64(
                    uuid,
                    filePayload.getBase64(),
                    filePayload.getFilename(),
                    filePayload.getContentType()
            );
        }
        throw new IllegalArgumentException("Reply file must contain url or base64 data");
    }

    /**
     * Upload a normalized reply file or voice payload.
     */
    public CdnUploadResponse uploadImageFile(String uuid, ReplyMediaItem filePayload) {
        if (filePayload == null) {
            throw new IllegalArgumentException("Reply file payload is empty");
        }
        if (StringUtils.hasText(filePayload.getUrl())) {
            return uploadImageByUrl(
                    uuid,
                    filePayload.getUrl(),
                    filePayload.getFilename(),
                    filePayload.getContentType()
            );
        }
        if (StringUtils.hasText(filePayload.getBase64())) {
            return uploadFileByBase64(
                    uuid,
                    filePayload.getBase64(),
                    filePayload.getFilename(),
                    filePayload.getContentType()
            );
        }
        throw new IllegalArgumentException("Reply file must contain url or base64 data");
    }

    /**
     * Extract the upload response body and convert API errors to exceptions.
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
     * Support both data:...;base64 and raw base64 payloads.
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

        String normalizedBase64 = normalizeBase64(encoded);
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(normalizedBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 file payload", e);
        }
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
     * Build a fallback filename from MIME type.
     */
    private String buildFilename(String contentType) {
        String extension = resolveExtension(contentType);
        if (!StringUtils.hasText(extension)) {
            extension = "bin";
        }
        return "reply-" + UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }

    private String normalizeBase64(String encoded) {
        String normalized = encoded == null ? "" : encoded.replaceAll("\\s", "");
        int remainder = normalized.length() % 4;
        if (remainder == 0) {
            return normalized;
        }
        StringBuilder builder = new StringBuilder(normalized);
        for (int i = remainder; i < 4; i++) {
            builder.append('=');
        }
        return builder.toString();
    }

    private String resolveExtension(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return null;
        }
        String normalized = contentType.trim().toLowerCase(Locale.ROOT);
        if ("audio/mpeg".equals(normalized) || "audio/mp3".equals(normalized) || "audio/x-mp3".equals(normalized)) {
            return "mp3";
        }
        if ("audio/mp4".equals(normalized) || "audio/x-m4a".equals(normalized)) {
            return "m4a";
        }
        if ("audio/wav".equals(normalized) || "audio/x-wav".equals(normalized) || "audio/wave".equals(normalized)) {
            return "wav";
        }
        if ("audio/amr".equals(normalized)) {
            return "amr";
        }
        if ("audio/silk".equals(normalized) || "application/silk".equals(normalized)) {
            return "silk";
        }
        if ("audio/ogg".equals(normalized)) {
            return "ogg";
        }
        if ("audio/webm".equals(normalized)) {
            return "webm";
        }

        int slashIndex = normalized.indexOf('/');
        if (slashIndex >= 0 && slashIndex < normalized.length() - 1) {
            return normalized.substring(slashIndex + 1).replaceAll("[^a-z0-9]", "");
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
