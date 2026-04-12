package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("groupUpdateStrategy")
public class GroupUpdateStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GroupUpdateStrategy.class);

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("handle group update callback, type={}", callbackRequest.getType());

        try {
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());

            WxGroupInfo wxGroupInfo = new WxGroupInfo();
            wxGroupInfo.setRoomId(jsonObject.getString("room_conversation_id"));
            wxGroupInfo.setNickname(jsonObject.getString("room_name"));
            wxGroupInfo.setFlag(jsonObject.getLong("flag"));
            wxGroupInfo.setCorpId(jsonObject.getLong("corp_id"));

            WxGroupInfo existingGroupInfo = wxGroupInfoService.selectByRoomId(wxGroupInfo.getRoomId());
            if (existingGroupInfo != null) {
                wxGroupInfo.setId(existingGroupInfo.getId());
                wxGroupInfo.setCorpId(existingGroupInfo.getCorpId());
                wxGroupInfo.setCreateUserId(existingGroupInfo.getCreateUserId());
                wxGroupInfo.setTotal(existingGroupInfo.getTotal());
                wxGroupInfo.setCreateTime(existingGroupInfo.getCreateTime());
                wxGroupInfoService.updateByPrimaryKey(wxGroupInfo);
                logger.info("group update applied, roomId={}", wxGroupInfo.getRoomId());
            } else {
                fillCorpIdIfAbsent(wxGroupInfo, callbackRequest.getUuid());
                if (wxGroupInfo.getCorpId() == null) {
                    logger.warn("skip group insert because corpId is missing, roomId={}", wxGroupInfo.getRoomId());
                    return "{\"success\": false, \"message\": \"corpId missing for group\"}";
                }
                wxGroupInfo.setCreateTime(System.currentTimeMillis());
                wxGroupInfoService.insert(wxGroupInfo);
                logger.info("group created from callback, roomId={}", wxGroupInfo.getRoomId());
            }

            return "{\"success\": true, \"message\": \"group update handled\"}";
        } catch (Exception e) {
            logger.error("failed to handle group update callback, type={}, json={}",
                    callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"group update failed\"}";
        }
    }

    private void fillCorpIdIfAbsent(WxGroupInfo groupInfo, String uuid) {
        if (groupInfo.getCorpId() != null) {
            return;
        }

        WxUserInfo ownerUserInfo = wxUserInfoService.selectByUuid(uuid);
        if (ownerUserInfo != null) {
            groupInfo.setCorpId(ownerUserInfo.getCorpid());
        }
    }
}
