package com.weichat.api.client;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.config.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class WxWorkApiClient {

    private static final Logger logger = LoggerFactory.getLogger(WxWorkApiClient.class);

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private RestTemplate restTemplate;

    public JSONObject getExternalContacts(String uuid, int limit, long seq) {
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("limit", limit);
        params.put("seq", seq);
        return post("/wxwork/GetExternalContacts", params);
    }

    public JSONObject getInnerContacts(String uuid, int limit, String strSeq) {
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("limit", limit);
        params.put("strSeq", strSeq);
        return post("/wxwork/GetInnerContacts", params);
    }

    public JSONObject getChatroomList(String uuid, int limit, int startIndex) {
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("limit", limit);
        params.put("star_index", startIndex);
        return post("/wxwork/GetChatroomMembers", params);
    }

    public JSONObject getRoomUserList(String uuid, long roomId) {
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("roomid", roomId);
        return post("/wxwork/GetRoomUserList", params);
    }

    public JSONObject syncMessage(String uuid, int limit, long seq) {
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("limit", limit);
        params.put("seq", seq);
        return post("/wxwork/SyncAllData", params);
    }

    public JSONObject post(String path, JSONObject params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(params.toJSONString(), headers);
        return executePost(path, entity);
    }

    /**
     * 使用 MultipartFile 转发 multipart/form-data 请求，适合 controller 直接接收上传文件后透传。
     */
    public JSONObject postMultipart(String path, String uuid, MultipartFile file, String fileFieldName) {
        try {
            return postMultipart(
                    path,
                    uuid,
                    fileFieldName,
                    resolveFilename(file.getOriginalFilename()),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build multipart request", e);
        }
    }

    /**
     * 使用原始字节数组转发 multipart/form-data 请求，给 base64 解码后的图片上传复用。
     */
    public JSONObject postMultipart(String path,
                                    String uuid,
                                    String fileFieldName,
                                    String filename,
                                    String contentType,
                                    byte[] fileBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("uuid", uuid);
        body.add(fileFieldName, buildFilePart(fileFieldName, filename, contentType, fileBytes));

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        return executePost(path, entity);
    }

    /**
     * 构造单个文件 part，统一补齐文件名和 content-type。
     */
    private HttpEntity<ByteArrayResource> buildFilePart(String fileFieldName,
                                                        String filename,
                                                        String contentType,
                                                        byte[] fileBytes) {
        final String resolvedFilename = resolveFilename(filename);
        ByteArrayResource resource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return resolvedFilename;
            }
        };

        HttpHeaders partHeaders = new HttpHeaders();
        if (contentType != null && !contentType.trim().isEmpty()) {
            partHeaders.setContentType(MediaType.parseMediaType(contentType));
        } else {
            partHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        partHeaders.setContentDispositionFormData(fileFieldName, resolvedFilename);
        return new HttpEntity<>(resource, partHeaders);
    }

    /**
     * multipart 上传时底层依赖文件名，这里兜底一个默认名。
     */
    private String resolveFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "upload.bin";
        }
        return filename;
    }

    /**
     * 统一执行下游 POST 请求，复用 JSON 和 multipart 两种场景。
     */
    private JSONObject executePost(String path, HttpEntity<?> entity) {
        String url = apiConfig.getApiBaseUrl() + path;
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);
            logger.info("API call succeeded: {}, response={}", path, response.getBody());
            return JSONObject.parseObject(response.getBody());
        } catch (Exception e) {
            logger.error("API call failed: {}", path, e);
            return null;
        }
    }
}
