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
import com.weichat.common.enums.MassTaskDetailSendStatusEnum;
import com.weichat.common.enums.MassTaskDetailSentFlagEnum;
import com.weichat.common.enums.MassTaskReceiverTypeEnum;
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

import java.util.List;

/**
 * 负责按单个接收对象执行群发任务，并回写发送结果。
 */
@Slf4j
@Service
public class MassMessageService {

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    /**
     * 根据接收对象类型路由到对应的发送逻辑。
     */
    public boolean sendMassMessageToReceiver(MassTaskDetail detail) {
        try {
            if (MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode().equals(detail.getReceiverType())) {
                return sendToExternalContact(detail);
            }
            if (MassTaskReceiverTypeEnum.GROUP_CHAT.getCode().equals(detail.getReceiverType())) {
                return sendToGroup(detail);
            }

            log.error("未知的接收方类型: {}", detail.getReceiverType());
            markFailure(detail, "未知的接收方类型");
            return false;
        } catch (Exception e) {
            log.error("发送群发消息时发生异常，明细ID: {}", detail.getId(), e);
            markFailure(detail, e.getMessage());
            return false;
        }
    }

    /**
     * 向单个外部联系人发送消息。
     */
    private boolean sendToExternalContact(MassTaskDetail detail) {
        try {
            MassTask task = massTaskService.getMassTaskById(detail.getTaskId());
            if (task == null) {
                markFailure(detail, "群发任务不存在");
                return false;
            }

            WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(detail.getReceiverId());
            if (friendInfo == null) {
                markFailure(detail, "接收好友不存在");
                return false;
            }

            WxUserInfo senderUserInfo = resolveSenderUserInfo(friendInfo.getOwnerUserId());
            if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
                markFailure(detail, "发送账号uuid不存在");
                return false;
            }

            SendTextRequest request = SendTextRequest.builder()
                    .uuid(senderUserInfo.getUuid())
                    .send_userid(friendInfo.getUserId())
                    .isRoom(false)
                    .content(resolveContent(task, resolveFriendName(friendInfo)))
                    .build();

            JSONObject result = wxWorkApiClient.post(
                    "/wxwork/SendTextMsg",
                    JSON.parseObject(JSON.toJSONString(request))
            );
            log.info("向外部联系人发送消息，结果: {}", result);
            if (result == null) {
                log.error("向外部联系人发送消息失败，下游接口无响应，receiverId={}, senderUuid={}", detail.getReceiverId(), senderUserInfo.getUuid());
                markFailure(detail, "下游发送接口无响应");
                return false;
            }

            Integer code = resolveResponseCode(result);
            if (code != null && code == 0) {
                markSuccess(detail);
                return true;
            }

            String errorMsg = resolveResponseMessage(result);
            log.error("向外部联系人发送消息失败，错误码: {}, 接收方ID: {}", code, friendInfo.getUserId());
            markFailure(detail, errorMsg != null ? errorMsg : "发送失败");
            return false;
        } catch (Exception e) {
            log.error("向外部联系人发送消息失败，接收方ID: {}", detail.getReceiverId(), e);
            markFailure(detail, e.getMessage());
            return false;
        }
    }

    /**
     * 向单个群聊发送消息。
     */
    private boolean sendToGroup(MassTaskDetail detail) {
        try {
            MassTask task = massTaskService.getMassTaskById(detail.getTaskId());
            if (task == null) {
                markFailure(detail, "群发任务不存在");
                return false;
            }

            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(detail.getReceiverId());
            if (groupInfo == null) {
                markFailure(detail, "群聊不存在");
                return false;
            }

            WxUserInfo senderUserInfo = resolveSenderUserInfo(groupInfo.getCreateUserId());
            if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
                markFailure(detail, "发送账号uuid不存在");
                return false;
            }

            Long roomId = parseLongSafely(groupInfo.getRoomId());
            if (roomId == null) {
                log.error("群ID格式不正确，无法发送消息，groupId: {}, roomId: {}", detail.getReceiverId(), groupInfo.getRoomId());
                markFailure(detail, "群ID格式不正确");
                return false;
            }

            SendTextRequest request = SendTextRequest.builder()
                    .uuid(senderUserInfo.getUuid())
                    .send_userid(roomId)
                    .isRoom(true)
                    .content(resolveContent(task, groupInfo.getNickname()))
                    .build();

            JSONObject result = wxWorkApiClient.post(
                    "/wxwork/SendTextMsg",
                    JSON.parseObject(JSON.toJSONString(request))
            );
            log.info("向群聊发送消息，结果: {}", result);
            if (result == null) {
                log.error("向群聊发送消息失败，下游接口无响应，groupId={}, senderUuid={}", detail.getReceiverId(), senderUserInfo.getUuid());
                markFailure(detail, "下游发送接口无响应");
                return false;
            }

            Integer code = resolveResponseCode(result);
            if (code != null && code == 0) {
                markSuccess(detail);
                return true;
            }

            String errorMsg = resolveResponseMessage(result);
            log.error("向群聊发送消息失败，错误码: {}, 群ID: {}", code, groupInfo.getRoomId());
            markFailure(detail, errorMsg != null ? errorMsg : "发送失败");
            return false;
        } catch (Exception e) {
            log.error("向群聊发送消息失败，群ID: {}", detail.getReceiverId(), e);
            markFailure(detail, e.getMessage());
            return false;
        }
    }

    /**
     * 有模板时渲染模板内容，否则直接使用任务内容。
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

    /**
     * 标记单条明细发送成功，并刷新任务统计。
     */
    private void markSuccess(MassTaskDetail detail) {
        massTaskDetailService.updateSendSuccessStatus(detail.getId());
        updateTaskStatistics(detail.getTaskId());
    }

    /**
     * 标记单条明细发送失败，并刷新任务统计。
     */
    private void markFailure(MassTaskDetail detail, String message) {
        massTaskDetailService.updateSendFailureStatus(detail.getId(), message != null ? message : "发送失败");
        updateTaskStatistics(detail.getTaskId());
    }

    /**
     * 重新统计任务的已发送数和成功数。
     */
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

    /**
     * 发送账号优先按 vid 匹配，找不到再回退到 userId。
     */
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
     * 安全解析群聊 roomId，避免非法值导致发送异常。
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
