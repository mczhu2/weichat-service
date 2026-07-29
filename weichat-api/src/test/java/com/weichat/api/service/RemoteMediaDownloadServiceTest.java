package com.weichat.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class RemoteMediaDownloadServiceTest {

    private static final byte[] IMAGE_BYTES = "image-bytes".getBytes(StandardCharsets.UTF_8);

    private RemoteMediaDownloadService remoteMediaDownloadService;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        remoteMediaDownloadService = new RemoteMediaDownloadService();
        ReflectionTestUtils.setField(remoteMediaDownloadService, "restTemplate", restTemplate);
    }

    @Test
    void shouldAppendJpgExtensionWhenUrlPathFilenameHasNoExtensionAndContentTypeIsJpeg() {
        String url = "https://img0.baidu.com/it/u=2907368631,1574533149&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=500";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(IMAGE_BYTES, MediaType.IMAGE_JPEG));

        RemoteMediaDownloadService.RemoteMediaResource resource = remoteMediaDownloadService.download(
                url,
                null,
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "image"
        );

        assertEquals("u=2907368631,1574533149&fm=253&fmt=auto&app=120&f=JPEG.jpg", resource.getFilename());
        assertTrue(resource.getFilename().endsWith(".jpg"));
        server.verify();
    }

    @Test
    void shouldPreserveUrlPathFilenameWhenItAlreadyHasAnExtension() {
        String url = "https://cdn.example.com/assets/photo.png?x-oss-process=image/resize,w_300";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(IMAGE_BYTES, MediaType.IMAGE_JPEG));

        RemoteMediaDownloadService.RemoteMediaResource resource = remoteMediaDownloadService.download(
                url,
                null,
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "image"
        );

        assertEquals("photo.png", resource.getFilename());
        server.verify();
    }
}
