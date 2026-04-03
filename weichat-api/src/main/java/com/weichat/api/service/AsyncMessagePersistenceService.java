package com.weichat.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.service.WxMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 消息异步持久化服务
 */
@Service
public class AsyncMessagePersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncMessagePersistenceService.class);

    private static final int TEXT_MESSAGE_TYPE = 2;

    @Autowired
    private WxMessageInfoService wxMessageInfoService;

    @Async("taskExecutor")
    public void saveSentTextMessage(SendTextRequest request, JSONObject response) {
        if (response == null || response.getIntValue("errcode") != 0) {
            return;
        }

        JSONObject data = response.getJSONObject("data");
        if (data == null) {
            logger.warn("发送文本消息成功但返回data为空，uuid={}, sendUserId={}", request.getUuid(), request.getSend_userid());
            return;
        }

        WxMessageInfo wxMessageInfo = buildTextMessageInfo(request, data);
        if (wxMessageInfo.getMsgId() == null) {
            logger.warn("发送文本消息异步入库跳过，msgId为空，uuid={}, response={}", request.getUuid(), response.toJSONString());
            return;
        }

        try {
            wxMessageInfoService.insert(wxMessageInfo);
        } catch (DuplicateKeyException e) {
            logger.info("文本消息已存在，跳过重复入库，msgId={}", wxMessageInfo.getMsgId());
        } catch (Exception e) {
            logger.error("文本消息异步入库失败，msgId={}, request={}", wxMessageInfo.getMsgId(), JSONObject.toJSONString(request), e);
        }
    }

    private WxMessageInfo buildTextMessageInfo(SendTextRequest request, JSONObject data) {
        WxMessageInfo wxMessageInfo = new WxMessageInfo();
        Long msgId = getLong(data, "msg_id", "msgId");
        Integer isRoom = getInteger(data, "is_room", "isRoom");

        wxMessageInfo.setMsgId(msgId);
        wxMessageInfo.setMessageId(msgId == null ? null : String.valueOf(msgId));
        wxMessageInfo.setMsgtype(defaultInteger(getInteger(data, "msgtype"), TEXT_MESSAGE_TYPE));
        wxMessageInfo.setFlag(getLong(data, "flag"));
        wxMessageInfo.setReceiver(defaultLong(getLong(data, "receiver"), request.getSend_userid()));
        wxMessageInfo.setSender(getLong(data, "sender"));
        wxMessageInfo.setSenderName(getString(data, "sender_name", "senderName"));
        wxMessageInfo.setIsRoom(defaultInteger(isRoom, Boolean.TRUE.equals(request.getIsRoom()) ? 1 : 0));
        wxMessageInfo.setRoomId(resolveRoomId(request, data, isRoom));
        wxMessageInfo.setServerId(getLong(data, "server_id", "serverId"));
        wxMessageInfo.setSendTime(getLong(data, "sendtime", "send_time", "createTime"));
        wxMessageInfo.setReferid(getLong(data, "referid"));
        wxMessageInfo.setAppInfo(getString(data, "app_info", "appInfo"));
        wxMessageInfo.setReaduinscount(getInteger(data, "readuinscount"));
        wxMessageInfo.setKfId(defaultLong(getLong(data, "kf_id", "kfId"), request.getKf_id()));
        wxMessageInfo.setContent(defaultString(getString(data, "content"), request.getContent()));
        wxMessageInfo.setIssync(getInteger(data, "issync"));
        wxMessageInfo.setAtList(resolveAtList(data));
        return wxMessageInfo;
    }

    private String resolveRoomId(SendTextRequest request, JSONObject data, Integer isRoom) {
        String roomId = getString(data, "room_conversation_id", "room_id", "roomId");
        if (roomId != null) {
            return roomId;
        }
        if (Boolean.TRUE.equals(request.getIsRoom()) || Integer.valueOf(1).equals(isRoom)) {
            Long sendUserid = request.getSend_userid();
            return sendUserid == null ? null : String.valueOf(sendUserid);
        }
        return null;
    }

    private String resolveAtList(JSONObject data) {
        JSONArray atList = data.getJSONArray("at_list");
        if (atList != null) {
            return atList.toJSONString();
        }
        return getString(data, "at_list", "atList");
    }

    private Long getLong(JSONObject data, String... keys) {
        for (String key : keys) {
            if (data.containsKey(key)) {
                return data.getLong(key);
            }
        }
        return null;
    }

    private Integer getInteger(JSONObject data, String... keys) {
        for (String key : keys) {
            if (data.containsKey(key)) {
                return data.getInteger(key);
            }
        }
        return null;
    }

    private String getString(JSONObject data, String... keys) {
        for (String key : keys) {
            if (data.containsKey(key)) {
                return data.getString(key);
            }
        }
        return null;
    }

    private Long defaultLong(Long value, Long defaultValue) {
        return value != null ? value : defaultValue;
    }

    private Integer defaultInteger(Integer value, Integer defaultValue) {
        return value != null ? value : defaultValue;
    }

    private String defaultString(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }
}
