package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.service.WxMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息处理策略
 */
@Component("messageStrategy")
public class MessageStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MessageStrategy.class);
    
    @Autowired
    private WxMessageInfoService wxMessageInfoService;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理消息回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON并保存消息
            WxMessageInfo wxMessageInfo = JSON.parseObject(callbackRequest.getJson(), WxMessageInfo.class);
            wxMessageInfoService.insert(wxMessageInfo);
            
            logger.info("消息处理成功");
            return "{\"success\": true, \"message\": \"消息处理成功\"}";
        } catch (Exception e) {
            logger.error("处理消息回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"消息处理失败\"}";
        }
    }
}