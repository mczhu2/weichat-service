package com.weichat.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

@Service
public class RemoteMediaDownloadService {

    @Autowired
    private RestTemplate restTemplate;

    public RemoteMediaResource download(String url,
                                        String preferredFilename,
                                        String defaultContentType,
                                        String defaultFilenamePrefix) {
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("Remote media url is empty");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );

        byte[] body = response.getBody();
        if (body == null || body.length == 0) {
            throw new IllegalStateException("Remote media response is empty");
        }

        String contentType = resolveContentType(response.getHeaders(), defaultContentType);
        String filename = resolveFilename(
                preferredFilename,
                response.getHeaders(),
                url,
                contentType,
                defaultFilenamePrefix
        );

        return new RemoteMediaResource(body, filename, contentType);
    }

    private String resolveContentType(HttpHeaders headers, String defaultContentType) {
        MediaType mediaType = headers.getContentType();
        if (mediaType != null) {
            return mediaType.toString();
        }
        return StringUtils.hasText(defaultContentType) ? defaultContentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    private String resolveFilename(String preferredFilename,
                                   HttpHeaders headers,
                                   String url,
                                   String contentType,
                                   String defaultFilenamePrefix) {
        if (StringUtils.hasText(preferredFilename)) {
            return preferredFilename;
        }

        String headerFilename = headers.getContentDisposition() == null ? null : headers.getContentDisposition().getFilename();
        if (StringUtils.hasText(headerFilename)) {
            return headerFilename;
        }

        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            String pathFilename = StringUtils.getFilename(path);
            if (StringUtils.hasText(pathFilename)) {
                return pathFilename;
            }
        } catch (Exception ignored) {
        }

        String prefix = StringUtils.hasText(defaultFilenamePrefix) ? defaultFilenamePrefix : "download";
        String extension = resolveExtension(contentType);
        if (!StringUtils.hasText(extension)) {
            extension = "bin";
        }
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }

    private String resolveExtension(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return null;
        }

        String normalized = contentType.trim().toLowerCase(Locale.ROOT);
        if ("image/jpeg".equals(normalized) || "image/jpg".equals(normalized) || "image/pjpeg".equals(normalized)) {
            return "jpg";
        }
        if ("image/png".equals(normalized) || "image/x-png".equals(normalized)) {
            return "png";
        }
        if ("image/gif".equals(normalized)) {
            return "gif";
        }
        if ("image/webp".equals(normalized)) {
            return "webp";
        }
        if ("image/bmp".equals(normalized) || "image/x-ms-bmp".equals(normalized)) {
            return "bmp";
        }
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

    public static class RemoteMediaResource {
        private final byte[] bytes;
        private final String filename;
        private final String contentType;

        public RemoteMediaResource(byte[] bytes, String filename, String contentType) {
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
