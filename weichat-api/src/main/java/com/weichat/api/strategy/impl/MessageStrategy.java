package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.service.CustomerReplyService;
import com.weichat.api.service.DownstreamMessageContentService;
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
 * 娑堟伅澶勭悊绛栫暐
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

    @Autowired
    private CustomerReplyService customerReplyService;

    @Autowired
    private DownstreamMessageContentService downstreamMessageContentService;

    @Value("${bizSystem.wecom.callback.url:http://115.190.61.17:8081/api/wecom/callback}")
    private String wecomCallbackUrl;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("Handle message callback. type={}", callbackRequest.getType());

        try {
            JSONObject json = JSON.parseObject(callbackRequest.getJson());
            WxMessageInfo wxMessageInfo = json.toJavaObject(WxMessageInfo.class);
            if (wxMessageInfo.getIsRoom() != null && wxMessageInfo.getIsRoom() == 1) {
                wxMessageInfo.setRoomId(json.getString("room_conversation_id"));
            }
            wxMessageInfoService.insert(wxMessageInfo);
            triggerWecomMessageCallback(wxMessageInfo);

            logger.info("Message callback handled successfully");
            return "{\"success\": true, \"message\": \"handled\"}";
        } catch (Exception e) {
            logger.error(
                    "Failed to handle message callback. type={}, json={}",
                    callbackRequest.getType(),
                    callbackRequest.getJson(),
                    e
            );
            return "{\"success\": false, \"message\": \"failed\"}";
        }
    }

    private void triggerWecomMessageCallback(WxMessageInfo wxMessageInfo) {
        if (wxMessageInfo == null || wxMessageInfo.getReceiver() == null) {
            return;
        }

        try {
            WxUserInfo receiverUser = wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver());
            if (!isValidReceiver(receiverUser, wxMessageInfo)) {
                return;
            }

            String content = downstreamMessageContentService.resolveCallbackContent(wxMessageInfo, receiverUser.getUuid());
            if (!StringUtils.hasText(content)) {
                logger.warn("Skip downstream callback because content is empty. msgId={}", wxMessageInfo.getMsgId());
                return;
            }

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    wecomCallbackUrl,
                    buildCallbackEntity(wxMessageInfo, receiverUser, content),
                    String.class
            );
            customerReplyService.sendReplyToCustomer(wxMessageInfo, receiverUser, responseEntity.getBody());

            logger.info(
                    "Downstream callback finished. msgId={}, receiver={}, sender={}, status={}, body={}",
                    wxMessageInfo.getMsgId(),
                    wxMessageInfo.getReceiver(),
                    wxMessageInfo.getSender(),
                    responseEntity.getStatusCodeValue(),
                    responseEntity.getBody()
            );
        } catch (Exception e) {
            logger.error(
                    "Downstream callback failed. msgId={}, receiver={}, sender={}",
                    wxMessageInfo.getMsgId(),
                    wxMessageInfo.getReceiver(),
                    wxMessageInfo.getSender(),
                    e
            );
        }
    }

    private boolean isValidReceiver(WxUserInfo receiverUser, WxMessageInfo wxMessageInfo) {
        if (receiverUser == null) {
            logger.warn("Skip downstream callback because receiver user is missing. receiver={}", wxMessageInfo.getReceiver());
            return false;
        }
        if (!StringUtils.hasText(receiverUser.getUuid())) {
            logger.warn(
                    "Skip downstream callback because receiver uuid is empty. receiver={}, userId={}",
                    wxMessageInfo.getReceiver(),
                    receiverUser.getUserId()
            );
            return false;
        }
        if (wxMessageInfo.getSender() == null) {
            logger.warn("Skip downstream callback because sender is empty. msgId={}", wxMessageInfo.getMsgId());
            return false;
        }
        return true;
    }

    /**
     * 组装发给下游业务系统的回调请求体。
     * content 字段已经由 DownstreamMessageContentService 做过统一收口，
     * 这里不再关心文本还是媒体消息，避免策略层继续膨胀。
     */
    private HttpEntity<String> buildCallbackEntity(WxMessageInfo wxMessageInfo, WxUserInfo receiverUser, String content) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("fromVid", String.valueOf(wxMessageInfo.getSender()));
        payload.put("content", content);
        payload.put("uuid", receiverUser.getUuid());
        payload.put("msgType", wxMessageInfo.getMsgtype() == null ? 0 : wxMessageInfo.getMsgtype());
        payload.put("nickname", wxMessageInfo.getSenderName());
        payload.put("fromName", wxMessageInfo.getSenderName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(JSON.toJSONString(payload), headers);
    }
}
