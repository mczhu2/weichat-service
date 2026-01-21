package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 外部好友变更策略
 */
@Component("friendChangeStrategy")
public class FriendChangeStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(FriendChangeStrategy.class);


    @Autowired
    private WxUserInfoService wxUserInfoService;
    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理外部好友变更回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());
            
            // 提取好友列表
            JSONArray friendList = jsonObject.getJSONArray("list");
            if (friendList == null || friendList.isEmpty()) {
                logger.info("外部好友变更回调，好友列表为空");
                return "{\"success\": true, \"message\": \"外部好友变更处理成功，好友列表为空\"}";
            }
            
            logger.info("开始处理外部好友变更，好友数量: {}", friendList.size());
            int successCount = 0;
            int failCount = 0;
            
            // 遍历好友列表，处理每个好友的变更
            for (Object friendObj : friendList) {
                JSONObject friendJson = (JSONObject) friendObj;
                try {
                    // 从JSON中提取好友信息
                    WxFriendInfo wxFriendInfo = JSON.toJavaObject(friendJson, WxFriendInfo.class);
                    WxUserInfo ownerUserInfo = wxUserInfoService.selectByUnionIdAndCorpId(callbackRequest.getUuid(), wxFriendInfo.getCorpId());
                    if(ownerUserInfo != null){
                        wxFriendInfo.setOwnerUserId(ownerUserInfo.getUserId());
                    }
                    // 处理好友变更
                    handleFriendChange(wxFriendInfo);
                    successCount++;
                } catch (Exception e) {
                    logger.error("处理单个好友变更失败，好友JSON: {}", friendJson, e);
                    failCount++;
                }
            }
            
            logger.info("外部好友变更处理完成，成功: {}, 失败: {}", successCount, failCount);
            return "{\"success\": true, \"message\": \"外部好友变更处理成功\"}";
        } catch (Exception e) {
            logger.error("处理外部好友变更回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"外部好友变更处理失败\"}";
        }
    }
    
    /**
     * 处理单个好友变更
     * @param wxFriendInfo 好友信息
     */
    private void handleFriendChange(WxFriendInfo wxFriendInfo) {
        // 根据status字段处理不同的变更类型
        int status = wxFriendInfo.getStatus();
        
        // 先根据unionid查询是否存在
        WxFriendInfo existingFriend = wxFriendInfoService.selectByUnionid(wxFriendInfo.getUnionid());
        
        if (status == 2049 || status == 8) {
            // 2049 或 8 代表被删除
            if (existingFriend != null) {
                // 存在则删除
                wxFriendInfoService.deleteByPrimaryKey(existingFriend.getId());
                logger.info("好友删除成功，unionid: {}", wxFriendInfo.getUnionid());
            }
        } else if (status == 9) {
            // 9 代表新增通知
            if (existingFriend == null) {
                // 不存在则新增
                wxFriendInfoService.insert(wxFriendInfo);
                logger.info("好友新增成功，unionid: {}", wxFriendInfo.getUnionid());
            }
        } else if (status == 2057) {
            // 2057 代表备注、标签、电话等信息变动
            if (existingFriend != null) {
                // 存在则更新
                wxFriendInfo.setId(existingFriend.getId());
                wxFriendInfoService.updateByPrimaryKey(wxFriendInfo);
                logger.info("好友更新成功，unionid: {}", wxFriendInfo.getUnionid());
            } else {
                // 不存在则新增
                wxFriendInfoService.insert(wxFriendInfo);
                logger.info("好友新增成功，unionid: {}", wxFriendInfo.getUnionid());
            }
        } else {
            // 其他状态，根据实际情况处理
            logger.info("未知好友状态: {}, unionid: {}", status, wxFriendInfo.getUnionid());
            // 对于未知状态，我们也可以选择更新或新增
            if (existingFriend != null) {
                wxFriendInfo.setId(existingFriend.getId());
                wxFriendInfoService.updateByPrimaryKey(wxFriendInfo);
            } else {
                wxFriendInfoService.insert(wxFriendInfo);
            }
        }
    }
}