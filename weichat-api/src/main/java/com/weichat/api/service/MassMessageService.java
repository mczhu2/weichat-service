package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.mass.sender.MassTaskMessageSender;
import com.weichat.api.service.mass.sender.MassTaskReceiverContext;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.enums.MassTaskDetailSendStatusEnum;
import com.weichat.common.enums.MassTaskDetailSentFlagEnum;
import com.weichat.common.enums.MassTaskReceiverTypeEnum;
import com.weichat.common.service.MassTaskDetailService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MassMessageService {

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired(required = false)
    private List<MassTaskMessageSender> senderList = Collections.emptyList();

    private Map<Integer, MassTaskMessageSender> senderMap = Collections.emptyMap();

    @PostConstruct
    private void initSenderMap() {
        Map<Integer, MassTaskMessageSender> senderMap = new HashMap<>();
        for (MassTaskMessageSender sender : senderList) {
            senderMap.put(sender.getMsgType(), sender);
        }
        this.senderMap = Collections.unmodifiableMap(senderMap);
    }

    public boolean sendMassMessageToReceiver(MassTaskDetail detail) {
        try {
            MassTask task = massTaskService.getMassTaskById(detail.getTaskId());
            if (task == null) {
                markFailure(detail, "mass task not found");
                return false;
            }

            MassTaskReceiverContext receiverContext = resolveReceiverContext(detail);
            if (receiverContext == null) {
                return false;
            }

            MassTaskMessageSender sender = senderMap.get(task.getMsgType());
            if (sender == null) {
                log.error("unsupported msgType: {}", task.getMsgType());
                markFailure(detail, "unsupported msgType: " + task.getMsgType());
                return false;
            }

            JSONObject result = sender.send(task, receiverContext);
            return handleSendResult(detail, result);
        } catch (Exception e) {
            log.error("mass message send failed, detailId={}", detail.getId(), e);
            markFailure(detail, e.getMessage());
            return false;
        }
    }

    private MassTaskReceiverContext resolveReceiverContext(MassTaskDetail detail) {
        if (MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode().equals(detail.getReceiverType())) {
            return resolveExternalContact(detail);
        }
        if (MassTaskReceiverTypeEnum.GROUP_CHAT.getCode().equals(detail.getReceiverType())) {
            return resolveGroupChat(detail);
        }
        markFailure(detail, "unsupported receiverType: " + detail.getReceiverType());
        return null;
    }

    private MassTaskReceiverContext resolveExternalContact(MassTaskDetail detail) {
        WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(detail.getReceiverId());
        if (friendInfo == null) {
            markFailure(detail, "friend not found");
            return null;
        }

        WxUserInfo senderUserInfo = resolveSenderUserInfo(friendInfo.getOwnerUserId());
        if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
            markFailure(detail, "sender uuid not found");
            return null;
        }

        return new MassTaskReceiverContext(
                senderUserInfo.getUuid(),
                friendInfo.getUserId(),
                false,
                resolveFriendName(friendInfo)
        );
    }

    private MassTaskReceiverContext resolveGroupChat(MassTaskDetail detail) {
        WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(detail.getReceiverId());
        if (groupInfo == null) {
            markFailure(detail, "group not found");
            return null;
        }

        WxUserInfo senderUserInfo = resolveSenderUserInfo(groupInfo.getCreateUserId());
        if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
            markFailure(detail, "sender uuid not found");
            return null;
        }

        Long roomId = parseLongSafely(groupInfo.getRoomId());
        if (roomId == null) {
            markFailure(detail, "invalid roomId");
            return null;
        }

        return new MassTaskReceiverContext(
                senderUserInfo.getUuid(),
                roomId,
                true,
                groupInfo.getNickname()
        );
    }

    private boolean handleSendResult(MassTaskDetail detail, JSONObject result) {
        if (result == null) {
            markFailure(detail, "downstream response is empty");
            return false;
        }

        Integer code = resolveResponseCode(result);
        if (code != null && code == 0) {
            markSuccess(detail);
            return true;
        }

        String responseMessage = resolveResponseMessage(result);
        markFailure(detail, StringUtils.hasText(responseMessage) ? responseMessage : "send failed");
        return false;
    }

    private void markSuccess(MassTaskDetail detail) {
        massTaskDetailService.updateSendSuccessStatus(detail.getId());
        updateTaskStatistics(detail.getTaskId());
    }

    private void markFailure(MassTaskDetail detail, String message) {
        massTaskDetailService.updateSendFailureStatus(detail.getId(), message);
        updateTaskStatistics(detail.getTaskId());
    }

    private void updateTaskStatistics(Long taskId) {
        MassTask task = massTaskService.getMassTaskById(taskId);
        if (task == null) {
            return;
        }

        List<MassTaskDetail> details = massTaskDetailService.getDetailsByTaskId(taskId);
        int sent = 0;
        int success = 0;
        for (MassTaskDetail detail : details) {
            if (MassTaskDetailSentFlagEnum.SENT.getCode().equals(detail.getIsSent())) {
                sent++;
                if (MassTaskDetailSendStatusEnum.SUCCESS.getCode().equals(detail.getSendStatus())) {
                    success++;
                }
            }
        }

        massTaskService.updateTaskStatistics(taskId, sent, success);
    }

    private Integer resolveResponseCode(JSONObject result) {
        if (result.containsKey("errcode")) {
            return result.getInteger("errcode");
        }
        return result.getInteger("code");
    }

    private String resolveResponseMessage(JSONObject result) {
        String errmsg = result.getString("errmsg");
        return StringUtils.hasText(errmsg) ? errmsg : result.getString("msg");
    }

    private WxUserInfo resolveSenderUserInfo(Long senderUserId) {
        if (senderUserId == null) {
            return null;
        }
        WxUserInfo userInfo = wxUserInfoService.selectByVid(senderUserId);
        return userInfo != null ? userInfo : wxUserInfoService.selectByUserId(senderUserId);
    }

    private String resolveFriendName(WxFriendInfo friendInfo) {
        if (friendInfo == null) {
            return "unknown";
        }
        if (StringUtils.hasText(friendInfo.getRealname())) {
            return friendInfo.getRealname();
        }
        return StringUtils.hasText(friendInfo.getNickname()) ? friendInfo.getNickname() : "unknown";
    }

    private Long parseLongSafely(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
