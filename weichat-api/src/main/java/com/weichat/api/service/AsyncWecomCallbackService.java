package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.weichat.api.vo.callback.DownstreamCallbackPayload;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AsyncWecomCallbackService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncWecomCallbackService.class);

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DownstreamMessageContentService downstreamMessageContentService;

    @Value("${bizSystem.wecom.callback.url:http://115.190.61.17:8081/api/wecom/callback}")
    private String wecomCallbackUrl;

    @Value("${bizSystem.wecom.reply-callback.url:http://47.115.229.106:8066/api/v1/message/wecomAgentReplyCallback}")
    private String wecomReplyCallbackUrl;

    @Async("taskExecutor")
    public void dispatch(WxMessageInfo wxMessageInfo) {
        if (wxMessageInfo == null || wxMessageInfo.getReceiver() == null) {
            return;
        }

        try {
            WxUserInfo receiverUser = wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver());
            if (!isValidReceiver(receiverUser, wxMessageInfo)) {
                return;
            }

            DownstreamCallbackPayload callbackPayload = downstreamMessageContentService.resolveCallbackPayload(
                    wxMessageInfo,
                    receiverUser.getUuid()
            );
            if (callbackPayload == null || !callbackPayload.hasPayload()) {
                logger.warn("Skip downstream callback because payload is empty. msgId={}", wxMessageInfo.getMsgId());
                return;
            }

            logger.info("Downstream callback payload resolved. msgId={}, receiver={}, payload={}",
                    wxMessageInfo.getMsgId(),
                    wxMessageInfo.getReceiver(),
                    JSON.toJSONString(callbackPayload));

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    wecomCallbackUrl,
                    buildCallbackEntity(wxMessageInfo, receiverUser, callbackPayload),
                    String.class
            );

            logger.info(
                    "Downstream callback accepted. msgId={}, receiver={}, sender={}, status={}, body={}",
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

    private HttpEntity<String> buildCallbackEntity(WxMessageInfo wxMessageInfo,
                                                   WxUserInfo receiverUser,
                                                   DownstreamCallbackPayload callbackPayload) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("fromVid", String.valueOf(wxMessageInfo.getSender()));
        payload.put("content", callbackPayload.getContent());
        payload.put("medias", callbackPayload.getMedias());
        payload.put("uuid", receiverUser.getUuid());
        payload.put("msgType", wxMessageInfo.getMsgtype() == null ? 0 : wxMessageInfo.getMsgtype());
        payload.put("nickname", wxMessageInfo.getSenderName());
        payload.put("fromName", wxMessageInfo.getSenderName());
        payload.put("replySender", wxMessageInfo.getReceiver());
        payload.put("replyReceiver", wxMessageInfo.getSender());
        payload.put("replyAccountUserId", wxMessageInfo.getReceiver());
        payload.put("kfId", wxMessageInfo.getKfId());
        payload.put("isRoom", StringUtils.hasText(wxMessageInfo.getRoomId()));
        payload.put("roomId", wxMessageInfo.getRoomId());
        if (StringUtils.hasText(wecomReplyCallbackUrl)) {
            payload.put("replyCallbackUrl", wecomReplyCallbackUrl);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(JSON.toJSONString(payload), headers);
    }
}
