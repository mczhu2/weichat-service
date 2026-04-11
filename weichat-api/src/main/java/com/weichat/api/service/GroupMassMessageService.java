package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.util.MessageTemplateUtil;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.MassTaskDetailService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.MessageTemplateService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private WxGroupInfoService wxGroupInfoService;

    public Long sendMassMessageToUsers(String uuid, List<Long> userIds, String content) {
        MassTask massTask = new MassTask();
        massTask.setTaskName("人员群发任务-" + System.currentTimeMillis());
        massTask.setTaskType(1);
        massTask.setContent(content);
        massTask.setMsgType(0);
        massTask.setCreator(uuid);
        massTask.setSendStatus(1);
        massTask.setCreateTime(LocalDateTime.now());

        return massTaskService.createMassTask(massTask, buildUserDetails(userIds));
    }

    public Long sendMassMessageToUsersWithTemplate(String uuid, List<Long> userIds, Long templateId, Map<String, Object> variables) {
        MessageTemplate template = messageTemplateService.getTemplateById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在，ID: " + templateId);
        }

        MassTask massTask = new MassTask();
        massTask.setTaskName("人员群发任务(模板)-" + System.currentTimeMillis());
        massTask.setTaskType(1);
        massTask.setContent(template.getTemplateContent());
        massTask.setMsgType(template.getMsgType());
        massTask.setCreator(uuid);
        massTask.setSendStatus(1);
        massTask.setCreateTime(LocalDateTime.now());
        massTask.setTemplateId(templateId);

        return massTaskService.createMassTask(massTask, buildUserDetails(userIds));
    }

    public Long sendMassMessageToGroups(String uuid, List<Long> groupIds, String content) {
        MassTask massTask = new MassTask();
        massTask.setTaskName("群聊群发任务-" + System.currentTimeMillis());
        massTask.setTaskType(2);
        massTask.setContent(content);
        massTask.setMsgType(0);
        massTask.setCreator(uuid);
        massTask.setSendStatus(1);
        massTask.setCreateTime(LocalDateTime.now());

        return massTaskService.createMassTask(massTask, buildGroupDetails(groupIds));
    }

    public Long sendMassMessageToGroupsWithTemplate(String uuid, List<Long> groupIds, Long templateId, Map<String, Object> variables) {
        MessageTemplate template = messageTemplateService.getTemplateById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在，ID: " + templateId);
        }

        MassTask massTask = new MassTask();
        massTask.setTaskName("群聊群发任务(模板)-" + System.currentTimeMillis());
        massTask.setTaskType(2);
        massTask.setContent(template.getTemplateContent());
        massTask.setMsgType(template.getMsgType());
        massTask.setCreator(uuid);
        massTask.setSendStatus(1);
        massTask.setCreateTime(LocalDateTime.now());
        massTask.setTemplateId(templateId);

        return massTaskService.createMassTask(massTask, buildGroupDetails(groupIds));
    }

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

            massTaskService.updateTaskStatus(taskId, 1);

            int successCount = 0;
            for (MassTaskDetail detail : details) {
                if (sendSingleMassMessage(detail)) {
                    successCount++;
                }
            }

            massTaskService.updateTaskStatistics(taskId, details.size(), successCount);
            massTaskService.updateTaskStatus(taskId, 2);
            return true;
        } catch (Exception e) {
            log.error("触发群发任务失败，任务ID: {}", taskId, e);
            massTaskService.updateTaskStatus(taskId, 3);
            return false;
        }
    }

    private List<MassTaskDetail> buildUserDetails(List<Long> userIds) {
        List<MassTaskDetail> details = new ArrayList<>();
        for (Long userId : userIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(1);
            detail.setReceiverId(userId);
            detail.setIsSent(0);
            detail.setSendStatus(0);
            detail.setCreateTime(LocalDateTime.now());

            WxUserInfo userInfo = wxUserInfoService.selectByPrimaryKey(userId);
            detail.setReceiverName(userInfo != null ? userInfo.getRealname() : "未知用户");
            details.add(detail);
        }
        return details;
    }

    private List<MassTaskDetail> buildGroupDetails(List<Long> groupIds) {
        List<MassTaskDetail> details = new ArrayList<>();
        for (Long groupId : groupIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(2);
            detail.setReceiverId(groupId);
            detail.setIsSent(0);
            detail.setSendStatus(0);
            detail.setCreateTime(LocalDateTime.now());

            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(groupId);
            detail.setReceiverName(groupInfo != null ? groupInfo.getNickname() : "未知群聊");
            details.add(detail);
        }
        return details;
    }

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

            if (detail.getReceiverType() == 1) {
                WxUserInfo userInfo = wxUserInfoService.selectByPrimaryKey(detail.getReceiverId());
                if (userInfo == null) {
                    log.error("找不到对应的用户信息，接收方ID: {}", detail.getReceiverId());
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "接收用户不存在");
                    return false;
                }
                sendUserId = userInfo.getUserId();
                receiverName = userInfo.getRealname();
                isRoom = false;
            } else if (detail.getReceiverType() == 2) {
                WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(detail.getReceiverId());
                if (groupInfo == null) {
                    log.error("找不到对应的群信息，群ID: {}", detail.getReceiverId());
                    massTaskDetailService.updateSendFailureStatus(detail.getId(), "群聊不存在");
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
            } else {
                log.error("未知的接收方类型: {}", detail.getReceiverType());
                massTaskDetailService.updateSendFailureStatus(detail.getId(), "未知的接收方类型");
                return false;
            }

            SendTextRequest request = SendTextRequest.builder()
                    .send_userid(sendUserId)
                    .isRoom(isRoom)
                    .content(resolveContent(task, receiverName))
                    .build();

            JSONObject result = wxWorkApiClient.post(
                    "/wxwork/sendtext",
                    JSON.parseObject(JSON.toJSONString(request))
            );
            log.info("发送群发消息，结果: {}", result);

            Integer code = result.getInteger("code");
            if (code != null && code == 0) {
                massTaskDetailService.updateSendSuccessStatus(detail.getId());
                return true;
            }

            String errorMsg = result.getString("msg");
            massTaskDetailService.updateSendFailureStatus(detail.getId(), errorMsg != null ? errorMsg : "发送失败");
            return false;
        } catch (Exception e) {
            log.error("发送单条群发消息失败，明细ID: {}", detail.getId(), e);
            massTaskDetailService.updateSendFailureStatus(detail.getId(), e.getMessage());
            return false;
        }
    }

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
