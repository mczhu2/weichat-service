package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.util.MessageTemplateUtil;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.enums.MassMessageTypeEnum;
import com.weichat.common.enums.MassTaskDetailSendStatusEnum;
import com.weichat.common.enums.MassTaskDetailSentFlagEnum;
import com.weichat.common.enums.MassTaskReceiverTypeEnum;
import com.weichat.common.enums.MassTaskSendStatusEnum;
import com.weichat.common.enums.MassTaskTypeEnum;
import com.weichat.common.service.MassTaskDetailService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.MessageTemplateService;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 负责创建群发任务，并按明细逐条触发实际发送。
 */
@Slf4j
@Service
public class GroupMassMessageService {

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired
    private MassMessageService massMessageService;

    /**
     * 创建面向外部联系人的文本群发任务。
     */
    public Long sendMassMessageToUsers(String uuid, List<Long> userIds, String content) {
        MassTask massTask = new MassTask();
        massTask.setTaskName("人员群发任务-" + System.currentTimeMillis());
        massTask.setTaskType(MassTaskTypeEnum.USER.getCode());
        massTask.setContent(content);
        massTask.setMsgType(MassMessageTypeEnum.TEXT.getCode());
        massTask.setCreator(uuid);
        massTask.setSendStatus(MassTaskSendStatusEnum.PENDING.getCode());
        massTask.setCreateTime(LocalDateTime.now());

        return massTaskService.createMassTask(massTask, buildUserDetails(userIds));
    }

    /**
     * 创建面向外部联系人的模板群发任务。
     */
    public Long sendMassMessageToUsersWithTemplate(String uuid, List<Long> userIds, Long templateId, Map<String, Object> variables) {
        MessageTemplate template = messageTemplateService.getTemplateById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在，ID: " + templateId);
        }

        MassTask massTask = new MassTask();
        massTask.setTaskName("人员群发任务(模板)-" + System.currentTimeMillis());
        massTask.setTaskType(MassTaskTypeEnum.USER.getCode());
        massTask.setContent(template.getTemplateContent());
        massTask.setMsgType(template.getMsgType());
        massTask.setCreator(uuid);
        massTask.setSendStatus(MassTaskSendStatusEnum.PENDING.getCode());
        massTask.setCreateTime(LocalDateTime.now());
        massTask.setTemplateId(templateId);

        return massTaskService.createMassTask(massTask, buildUserDetails(userIds));
    }

    /**
     * 创建面向群聊的文本群发任务。
     */
    public Long sendMassMessageToGroups(String uuid, List<Long> groupIds, String content) {
        MassTask massTask = new MassTask();
        massTask.setTaskName("群聊群发任务-" + System.currentTimeMillis());
        massTask.setTaskType(MassTaskTypeEnum.GROUP.getCode());
        massTask.setContent(content);
        massTask.setMsgType(MassMessageTypeEnum.TEXT.getCode());
        massTask.setCreator(uuid);
        massTask.setSendStatus(MassTaskSendStatusEnum.PENDING.getCode());
        massTask.setCreateTime(LocalDateTime.now());

        return massTaskService.createMassTask(massTask, buildGroupDetails(groupIds));
    }

    /**
     * 创建面向群聊的模板群发任务。
     */
    public Long sendMassMessageToGroupsWithTemplate(String uuid, List<Long> groupIds, Long templateId, Map<String, Object> variables) {
        MessageTemplate template = messageTemplateService.getTemplateById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在，ID: " + templateId);
        }

        MassTask massTask = new MassTask();
        massTask.setTaskName("群聊群发任务(模板)-" + System.currentTimeMillis());
        massTask.setTaskType(MassTaskTypeEnum.GROUP.getCode());
        massTask.setContent(template.getTemplateContent());
        massTask.setMsgType(template.getMsgType());
        massTask.setCreator(uuid);
        massTask.setSendStatus(MassTaskSendStatusEnum.PENDING.getCode());
        massTask.setCreateTime(LocalDateTime.now());
        massTask.setTemplateId(templateId);

        return massTaskService.createMassTask(massTask, buildGroupDetails(groupIds));
    }

    /**
     * 顺序执行任务下的每条明细，并回写任务状态。
     */
    public boolean triggerMassTask(Long taskId) {
        try {
            MassTask task = massTaskService.getMassTaskById(taskId);
            if (task == null) {
                log.error("找不到对应的群发任务，任务ID: {}", taskId);
                return false;
            }

            List<MassTaskDetail> details = massTaskDetailService.getDetailsByTaskId(taskId);
            if (details.isEmpty()) {
                log.warn("任务没有对应的明细记录，任务ID: {}", taskId);
                return false;
            }

            massTaskService.updateTaskStatus(taskId, MassTaskSendStatusEnum.SENDING.getCode());

            int successCount = 0;
            for (MassTaskDetail detail : details) {
                if (massMessageService.sendMassMessageToReceiver(detail)) {
                    successCount++;
                }
            }

            massTaskService.updateTaskStatistics(taskId, details.size(), successCount);
            massTaskService.updateTaskStatus(taskId, MassTaskSendStatusEnum.COMPLETED.getCode());
            return true;
        } catch (Exception e) {
            log.error("触发群发任务失败，任务ID: {}", taskId, e);
            massTaskService.updateTaskStatus(taskId, MassTaskSendStatusEnum.CANCELLED.getCode());
            return false;
        }
    }

    /**
     * 根据用户 ID 列表构造待发送明细。
     */
    private List<MassTaskDetail> buildUserDetails(List<Long> userIds) {
        List<MassTaskDetail> details = new ArrayList<>();
        for (Long userId : userIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode());
            detail.setReceiverId(userId);
            detail.setIsSent(MassTaskDetailSentFlagEnum.UNSENT.getCode());
            detail.setSendStatus(MassTaskDetailSendStatusEnum.UNSUCCESSFUL.getCode());
            detail.setCreateTime(LocalDateTime.now());

            WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(userId);
            detail.setReceiverName(resolveFriendName(friendInfo));
            details.add(detail);
        }
        return details;
    }

    /**
     * 根据群聊 ID 列表构造待发送明细。
     */
    private List<MassTaskDetail> buildGroupDetails(List<Long> groupIds) {
        List<MassTaskDetail> details = new ArrayList<>();
        for (Long groupId : groupIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(MassTaskReceiverTypeEnum.GROUP_CHAT.getCode());
            detail.setReceiverId(groupId);
            detail.setIsSent(MassTaskDetailSentFlagEnum.UNSENT.getCode());
            detail.setSendStatus(MassTaskDetailSendStatusEnum.UNSUCCESSFUL.getCode());
            detail.setCreateTime(LocalDateTime.now());

            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(groupId);
            detail.setReceiverName(groupInfo != null ? groupInfo.getNickname() : "未知群聊");
            details.add(detail);
        }
        return details;
    }

    /**
     * 执行单条明细发送。
     */
    private boolean sendSingleMassMessage(MassTaskDetail detail) {
        try {
            MassTask task = massTaskService.getMassTaskById(detail.getTaskId());
            if (task == null) {
                log.error("找不到对应的群发任务，任务ID: {}", detail.getTaskId());
                massTaskDetailService.updateSendFailureStatus(detail.getId(), "群发任务不存在");
                return false;
            }

            Long sendUserId;
            boolean isRoom;
            String receiverName;

            if (MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode().equals(detail.getReceiverType())) {
                WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(detail.getReceiverId());
                if (friendInfo == null) {
                    log.error("找不到对应的好友信息，接收方ID: {}", detail.getReceiverId());
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "接收好友不存在");
                    return false;
                }
                WxUserInfo senderUserInfo = resolveSenderUserInfo(friendInfo.getOwnerUserId());
                if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "发送账号uuid不存在");
                    return false;
                }
                sendUserId = friendInfo.getUserId();
                receiverName = resolveFriendName(friendInfo);
                isRoom = false;
                task.setCreator(senderUserInfo.getUuid());
            } else if (MassTaskReceiverTypeEnum.GROUP_CHAT.getCode().equals(detail.getReceiverType())) {
                WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(detail.getReceiverId());
                if (groupInfo == null) {
                    log.error("找不到对应的群信息，群ID: {}", detail.getReceiverId());
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "群聊不存在");
                    return false;
                }
                WxUserInfo senderUserInfo = resolveSenderUserInfo(groupInfo.getCreateUserId());
                if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "发送账号uuid不存在");
                    return false;
                }
                sendUserId = parseLongSafely(groupInfo.getRoomId());
                if (sendUserId == null) {
                    log.error("群ID格式不正确，无法发送消息，groupId: {}, roomId: {}", detail.getReceiverId(), groupInfo.getRoomId());
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "群ID格式不正确");
                    return false;
                }
                receiverName = groupInfo.getNickname();
                isRoom = true;
                task.setCreator(senderUserInfo.getUuid());
            } else {
                log.error("未知的接收方类型: {}", detail.getReceiverType());
                massTaskDetailService.updateSendFailureStatus(detail.getId(), "未知的接收方类型");
                return false;
            }

            SendTextRequest request = SendTextRequest.builder()
                    .uuid(task.getCreator())
                    .send_userid(sendUserId)
                    .isRoom(isRoom)
                    .content(resolveContent(task, receiverName))
                    .build();

            JSONObject result = wxWorkApiClient.post(
                    "/wxwork/SendTextMsg",
                    JSON.parseObject(JSON.toJSONString(request))
            );
            log.info("发送群发消息，结果: {}", result);
            if (result == null) {
                massTaskDetailService.updateSendFailureStatus(detail.getId(), "下游发送接口无响应");
                return false;
            }

            Integer code = resolveResponseCode(result);
            if (code != null && code == 0) {
                massTaskDetailService.updateSendSuccessStatus(detail.getId());
                return true;
            }

            String errorMsg = resolveResponseMessage(result);
            massTaskDetailService.updateSendFailureStatus(detail.getId(), errorMsg != null ? errorMsg : "发送失败");
            return false;
        } catch (Exception e) {
            log.error("发送单条群发消息失败，明细ID: {}", detail.getId(), e);
            massTaskDetailService.updateSendFailureStatus(detail.getId(), e.getMessage());
            return false;
        }
    }

    /**
     * 有模板时渲染模板内容，否则直接使用任务原始内容。
     */
    private String resolveContent(MassTask task, String receiverName) {
        if (task.getTemplateId() == null) {
            return task.getContent();
        }

        MessageTemplate template = messageTemplateService.getTemplateById(task.getTemplateId());
        if (template == null) {
            return task.getContent();
        }

        return MessageTemplateUtil.renderTemplate(template.getTemplateContent(), receiverName);
    }

    private Integer resolveResponseCode(JSONObject result) {
        if (result == null) {
            return null;
        }
        if (result.containsKey("errcode")) {
            return result.getInteger("errcode");
        }
        return result.getInteger("code");
    }

    private String resolveResponseMessage(JSONObject result) {
        if (result == null) {
            return null;
        }
        String errorMsg = result.getString("errmsg");
        return StringUtils.hasText(errorMsg) ? errorMsg : result.getString("msg");
    }

    private WxUserInfo resolveSenderUserInfo(Long senderUserId) {
        if (senderUserId == null) {
            return null;
        }
        WxUserInfo userInfo = wxUserInfoService.selectByVid(senderUserId);
        if (userInfo != null) {
            return userInfo;
        }
        return wxUserInfoService.selectByUserId(senderUserId);
    }

    private String resolveFriendName(WxFriendInfo friendInfo) {
        if (friendInfo == null) {
            return "未知用户";
        }
        if (StringUtils.hasText(friendInfo.getRealname())) {
            return friendInfo.getRealname();
        }
        if (StringUtils.hasText(friendInfo.getNickname())) {
            return friendInfo.getNickname();
        }
        return "未知用户";
    }

    /**
     * 安全解析群聊 roomId。
     */
    private Long parseLongSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Failed to parse roomId: {}", value);
            return null;
        }
    }
}
