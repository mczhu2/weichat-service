package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxGroupMember;
import com.weichat.common.service.WxGroupMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 群成员新增策略
 */
@Component("groupMemberAddStrategy")
public class GroupMemberAddStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GroupMemberAddStrategy.class);
    
    @Autowired
    private WxGroupMemberService wxGroupMemberService;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理群成员新增回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON并保存群成员
            WxGroupMember groupMember = JSON.parseObject(callbackRequest.getJson(), WxGroupMember.class);
            wxGroupMemberService.insert(groupMember);
            
            logger.info("群成员新增处理成功");
            return "{\"success\": true, \"message\": \"群成员新增处理成功\"}";
        } catch (Exception e) {
            logger.error("处理群成员新增回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"群成员新增处理失败\"}";
        }
    }
}