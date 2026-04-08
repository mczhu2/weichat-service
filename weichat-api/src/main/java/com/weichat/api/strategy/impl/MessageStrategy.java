package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxMessageInfoService;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息处理策略
 */
@Component("messageStrategy")
public class MessageStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MessageStrategy.class);
    
    @Autowired
    private WxMessageInfoService wxMessageInfoService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bizSystem.wecom.callback.url:http://115.190.61.17:8081/api/wecom/callback}")
    private String wecomCallbackUrl;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理消息回调，type: {}", callbackRequest.getType());
        
        try {
            JSONObject json = JSON.parseObject(callbackRequest.getJson());
            WxMessageInfo wxMessageInfo = json.toJavaObject(WxMessageInfo.class);
            if (wxMessageInfo.getIsRoom() != null && wxMessageInfo.getIsRoom() == 1) {
                wxMessageInfo.setRoomId(json.getString("room_conversation_id"));
            }
            wxMessageInfoService.insert(wxMessageInfo);
            triggerWecomMessageCallback(wxMessageInfo);
            
            logger.info("消息处理成功");
            return "{\"success\": true, \"message\": \"消息处理成功\"}";
        } catch (Exception e) {
            logger.error("处理消息回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"消息处理失败\"}";
        }
    }

    private void triggerWecomMessageCallback(WxMessageInfo wxMessageInfo) {
        if (wxMessageInfo == null || wxMessageInfo.getReceiver() == null) {
            return;
        }
        try {
            WxUserInfo receiverUser = wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver());
            if (receiverUser == null) {
                logger.warn("企微消息回调跳过，未找到有效接收账号，receiver: {}", wxMessageInfo.getReceiver());
                return;
            }
            if (!StringUtils.hasText(receiverUser.getUuid())) {
                logger.warn("企微消息回调跳过，接收账号uuid为空，receiver: {}, userId: {}", wxMessageInfo.getReceiver(), receiverUser.getUserId());
                return;
            }
            if (wxMessageInfo.getSender() == null) {
                logger.warn("企微消息回调跳过，发送人为空，msgId: {}", wxMessageInfo.getMsgId());
                return;
            }
            String content = resolveMessageContent(wxMessageInfo);
            if (!StringUtils.hasText(content)) {
                logger.warn("企微消息回调跳过，消息内容为空，msgId: {}", wxMessageInfo.getMsgId());
                return;
            }

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("fromVid", String.valueOf(wxMessageInfo.getSender()));
            payload.put("content", content);
            payload.put("uuid", receiverUser.getUuid());
            payload.put("msgType", wxMessageInfo.getMsgtype() == null ? 0 : wxMessageInfo.getMsgtype());
            payload.put("nickname", wxMessageInfo.getSenderName());
            payload.put("fromName", wxMessageInfo.getSenderName());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(payload), headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(wecomCallbackUrl, requestEntity, String.class);
            logger.info(
                    "企微消息回调完成，msgId: {}, receiver: {}, sender: {}, status: {}, body: {}",
                    wxMessageInfo.getMsgId(),
                    wxMessageInfo.getReceiver(),
                    wxMessageInfo.getSender(),
                    responseEntity.getStatusCodeValue(),
                    responseEntity.getBody()
            );
        } catch (Exception e) {
            logger.error(
                    "企微消息回调失败，msgId: {}, receiver: {}, sender: {}",
                    wxMessageInfo.getMsgId(),
                    wxMessageInfo.getReceiver(),
                    wxMessageInfo.getSender(),
                    e
            );
        }
    }

    private String resolveMessageContent(WxMessageInfo wxMessageInfo) {
        if (StringUtils.hasText(wxMessageInfo.getContent())) {
            return wxMessageInfo.getContent();
        }
        if (StringUtils.hasText(wxMessageInfo.getTitle())) {
            return wxMessageInfo.getTitle();
        }
        if (StringUtils.hasText(wxMessageInfo.getDesc())) {
            return wxMessageInfo.getDesc();
        }
        if (StringUtils.hasText(wxMessageInfo.getUrl())) {
            return wxMessageInfo.getUrl();
        }
        if (StringUtils.hasText(wxMessageInfo.getFileId())) {
            return wxMessageInfo.getFileId();
        }
        if (StringUtils.hasText(wxMessageInfo.getMessageId())) {
            return wxMessageInfo.getMessageId();
        }
        return "";
    }
}
