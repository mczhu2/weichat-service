package com.weichat.api.client;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.config.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        String url = apiConfig.getApiBaseUrl() + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(params.toJSONString(), headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);
            logger.info("API调用成功: {},返回结果{}", path, response.getBody());
            return JSONObject.parseObject(response.getBody());
        } catch (Exception e) {
            logger.error("API调用失败: {}", path, e);
            return null;
        }
    }
}
