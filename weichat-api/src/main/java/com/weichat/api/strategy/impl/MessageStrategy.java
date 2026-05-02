package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.service.AsyncWecomCallbackService;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.service.WxMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Message callback strategy.
 */
@Component("messageStrategy")
public class MessageStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MessageStrategy.class);

    @Autowired
    private WxMessageInfoService wxMessageInfoService;

    @Autowired
    private AsyncWecomCallbackService asyncWecomCallbackService;

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

            // Only the original inbound message should trigger the downstream agent callback.
            if (Long.valueOf(0L).equals(wxMessageInfo.getReferid())) {
                try {
                    asyncWecomCallbackService.dispatch(wxMessageInfo);
                } catch (Exception callbackException) {
                    logger.error("Failed to enqueue downstream callback. msgId={}", wxMessageInfo.getMsgId(), callbackException);
                }
            }

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
}
