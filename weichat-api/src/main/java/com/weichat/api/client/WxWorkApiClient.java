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

    public JSONObject postMultipart(String path, String uuid, MultipartFile file, String fileFieldName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("uuid", uuid);
        body.add(fileFieldName, buildFilePart(file, fileFieldName));

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        return executePost(path, entity);
    }

    private HttpEntity<ByteArrayResource> buildFilePart(MultipartFile file, String fileFieldName) {
        try {
            String originalFilename = file.getOriginalFilename();
            final String filename = (originalFilename == null || originalFilename.trim().isEmpty())
                ? "upload.bin" : originalFilename;

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };

            HttpHeaders partHeaders = new HttpHeaders();
            String contentType = file.getContentType();
            if (contentType != null && !contentType.trim().isEmpty()) {
                partHeaders.setContentType(MediaType.parseMediaType(contentType));
            } else {
                partHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            partHeaders.setContentDispositionFormData(fileFieldName, filename);
            return new HttpEntity<>(resource, partHeaders);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build multipart request", e);
        }
    }

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
