package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.service.WxGroupInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 群更新策略
 */
@Component("groupUpdateStrategy")
public class GroupUpdateStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GroupUpdateStrategy.class);
    
    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理群更新回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());
            
            // 从群更新消息中提取群信息，注意room_id字段是room_conversation_id
            WxGroupInfo wxGroupInfo = new WxGroupInfo();
            wxGroupInfo.setRoomId(jsonObject.getString("room_conversation_id"));
            wxGroupInfo.setNickname(jsonObject.getString("room_name"));
            wxGroupInfo.setFlag(jsonObject.getLong("flag"));
            
            // 先根据roomId查询是否存在
            WxGroupInfo existingGroupInfo = wxGroupInfoService.selectByRoomId(wxGroupInfo.getRoomId());
            if (existingGroupInfo != null) {
                // 存在则更新，设置主键ID和其他可能的字段
                wxGroupInfo.setId(existingGroupInfo.getId());
                // 可以根据需要更新其他字段，这里只更新了主要字段
                wxGroupInfo.setCreateUserId(existingGroupInfo.getCreateUserId());
                wxGroupInfo.setTotal(existingGroupInfo.getTotal());
                wxGroupInfo.setCreateTime(existingGroupInfo.getCreateTime());
                wxGroupInfoService.updateByPrimaryKey(wxGroupInfo);
                logger.info("群更新处理成功，群ID: {}", wxGroupInfo.getRoomId());
            } else {
                // 不存在则新增，设置必要的默认值
                wxGroupInfo.setCreateTime(System.currentTimeMillis());
                wxGroupInfoService.insert(wxGroupInfo);
                logger.info("群更新处理成功，新增群信息，群ID: {}", wxGroupInfo.getRoomId());
            }
            
            return "{\"success\": true, \"message\": \"群更新处理成功\"}";
        } catch (Exception e) {
            logger.error("处理群更新回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"群更新处理失败\"}";
        }
    }
}