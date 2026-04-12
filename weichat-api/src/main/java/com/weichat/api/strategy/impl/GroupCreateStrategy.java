package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxGroupMember;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxGroupMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 建群和群成员列表策略
 */
@Component("groupCreateStrategy")
public class GroupCreateStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GroupCreateStrategy.class);
    
    @Autowired
    private WxGroupInfoService wxGroupInfoService;
    
    @Autowired
    private WxGroupMemberService wxGroupMemberService;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理建群和群成员列表回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());
            
            // 保存群信息
            WxGroupInfo wxGroupInfo = JSON.toJavaObject(jsonObject, WxGroupInfo.class);
            wxGroupInfo.setCorpId(jsonObject.getLong("corp_id"));
            // 先根据roomId查询是否存在
            WxGroupInfo existingGroupInfo = wxGroupInfoService.selectByRoomId(wxGroupInfo.getRoomId());
            if (existingGroupInfo != null) {
                // 存在则更新，设置主键ID
                wxGroupInfo.setId(existingGroupInfo.getId());
                wxGroupInfoService.updateByPrimaryKey(wxGroupInfo);
                logger.info("群信息更新成功");
            } else {
                // 不存在则新增
                wxGroupInfoService.insert(wxGroupInfo);
                logger.info("群信息新增成功");
            }
            
            // 处理群成员列表
            if (jsonObject.containsKey("member_list")) {
                JSONArray memberList = jsonObject.getJSONArray("member_list");
                String roomId = wxGroupInfo.getRoomId();
                int successCount = 0;
                
                for (Object member : memberList) {
                    WxGroupMember groupMember = JSON.toJavaObject((JSONObject) member, WxGroupMember.class);
                    groupMember.setRoomId(roomId); // 设置群ID
                    
                    // 先根据roomId和uin查询是否存在
                    WxGroupMember existingMember = wxGroupMemberService.selectByRoomIdAndUin(roomId, groupMember.getUin());
                    if (existingMember != null) {
                        // 存在则更新，设置主键ID
                        groupMember.setId(existingMember.getId());
                        wxGroupMemberService.updateByPrimaryKey(groupMember);
                    } else {
                        // 不存在则新增
                        wxGroupMemberService.insert(groupMember);
                    }
                    successCount++;
                }
                
                logger.info("群成员列表处理成功，成员数量: {}", successCount);
            }
            
            return "{\"success\": true, \"message\": \"建群和群成员列表处理成功\"}";
        } catch (Exception e) {
            logger.error("处理建群和群成员列表回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"建群和群成员列表处理失败\"}";
        }
    }
}
