package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.GroupMassMessageService;
import com.weichat.api.service.MassMessageService;
import com.weichat.api.service.mass.MassMessageTypeSpecService;
import com.weichat.api.service.mass.MassTaskPlanExecutionService;
import com.weichat.api.service.mass.MassTaskRequestValidator;
import com.weichat.api.vo.request.mass.MassTaskCreateRequest;
import com.weichat.api.vo.request.mass.MassTaskPlanCreateRequest;
import com.weichat.api.vo.response.mass.MassMessageTypeSpecResponse;
import com.weichat.api.vo.response.mass.MassTaskValidationResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.MassTaskPlan;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.enums.MassTaskDetailSendStatusEnum;
import com.weichat.common.enums.MassTaskDetailSentFlagEnum;
import com.weichat.common.enums.MassTaskReceiverTypeEnum;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.MassTaskDetailService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 群发任务控制器，负责创建、触发和管理群发任务。
 */
@Api(tags = "群发功能")
@RestController
@RequestMapping("/api/v1/mass")
public class MassMessageController {

    @Autowired
    private GroupMassMessageService groupMassMessageService;

    @Autowired
    private MassMessageService massMessageService;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired
    private MassTaskRequestValidator massTaskRequestValidator;

    @Autowired
    private MassMessageTypeSpecService massMessageTypeSpecService;

    @Autowired
    private MassTaskPlanExecutionService massTaskPlanExecutionService;

    /**
     * 创建面向外部联系人的文本群发任务。
     */
    @ApiOperation("向多个用户群发消息")
    @PostMapping("/send-to-users")
    public ApiResult<Long> sendMassMessageToUsers(@RequestParam String uuid,
                                                  @RequestParam List<Long> userIds,
                                                  @RequestParam String content) {
        Long taskId = groupMassMessageService.sendMassMessageToUsers(uuid, userIds, content);
        return ApiResult.success(taskId);
    }

    /**
     * 创建面向外部联系人的模板群发任务。
     */
    @ApiOperation("向多个用户群发消息（使用模板）")
    @PostMapping("/send-to-users-with-template")
    public ApiResult<Long> sendMassMessageToUsersWithTemplate(@RequestParam String uuid,
                                                              @RequestParam List<Long> userIds,
                                                              @RequestParam Long templateId,
                                                              @RequestBody(required = false) Map<String, Object> variables) {
        Long taskId = groupMassMessageService.sendMassMessageToUsersWithTemplate(uuid, userIds, templateId, variables);
        return ApiResult.success(taskId);
    }

    /**
     * 创建面向群聊的文本群发任务。
     */
    @ApiOperation("向多个群聊群发消息")
    @PostMapping("/send-to-groups")
    public ApiResult<Long> sendMassMessageToGroups(@RequestParam String uuid,
                                                   @RequestParam List<Long> groupIds,
                                                   @RequestParam String content) {
        Long taskId = groupMassMessageService.sendMassMessageToGroups(uuid, groupIds, content);
        return ApiResult.success(taskId);
    }

    /**
     * 创建面向群聊的模板群发任务。
     */
    @ApiOperation("向多个群聊群发消息（使用模板）")
    @PostMapping("/send-to-groups-with-template")
    public ApiResult<Long> sendMassMessageToGroupsWithTemplate(@RequestParam String uuid,
                                                               @RequestParam List<Long> groupIds,
                                                               @RequestParam Long templateId,
                                                               @RequestBody(required = false) Map<String, Object> variables) {
        Long taskId = groupMassMessageService.sendMassMessageToGroupsWithTemplate(uuid, groupIds, templateId, variables);
        return ApiResult.success(taskId);
    }

    /**
     * 手动触发已创建的群发任务执行发送。
     */
    @ApiOperation("手动触发群发任务")
    @PostMapping("/trigger-task/{taskId}")
    public ApiResult<Boolean> triggerMassTask(@PathVariable Long taskId) {
        boolean result = groupMassMessageService.triggerMassTask(taskId);
        return ApiResult.success(result);
    }

    @ApiOperation("获取消息类型配置")
    @GetMapping("/message-type-specs")
    public ApiResult<List<MassMessageTypeSpecResponse>> getMessageTypeSpecs() {
        return ApiResult.success(massMessageTypeSpecService.getAllSpecs());
    }

    @ApiOperation("校验群发任务请求")
    @PostMapping("/task/validate")
    public ApiResult<MassTaskValidationResponse> validateMassTask(@RequestBody MassTaskCreateRequest request) {
        MassTaskValidationResponse response = new MassTaskValidationResponse();
        List<String> errors = massTaskRequestValidator.validateCreateRequest(request);
        response.setValid(errors.isEmpty());
        response.setErrors(errors);
        return ApiResult.success(response);
    }

    @ApiOperation("创建群发计划")
    @PostMapping("/plans")
    public ApiResult<Long> createMassTaskPlan(@RequestBody MassTaskPlanCreateRequest request) {
        try {
            return ApiResult.success(massTaskPlanExecutionService.createPlan(request));
        } catch (IllegalArgumentException ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }

    @ApiOperation("获取群发计划列表")
    @GetMapping("/plans")
    public ApiResult<List<MassTaskPlan>> getMassTaskPlanList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<MassTaskPlan> plans = massTaskPlanExecutionService.getPlanList(offset, pageSize);
        int total = massTaskPlanExecutionService.getPlanTotal();
        return ApiResult.success(plans, total);
    }

    @ApiOperation("获取群发计划详情")
    @GetMapping("/plan/{planId}")
    public ApiResult<MassTaskPlan> getMassTaskPlan(@PathVariable Long planId) {
        MassTaskPlan plan = massTaskPlanExecutionService.getPlanById(planId);
        if (plan == null) {
            return ApiResult.fail("群发计划不存在");
        }
        return ApiResult.success(plan);
    }

    @ApiOperation("更新群发计划状态")
    @PutMapping("/plan/{planId}/status")
    public ApiResult<Void> updateMassTaskPlanStatus(@PathVariable Long planId, @RequestParam Integer status) {
        try {
            massTaskPlanExecutionService.updatePlanStatus(planId, status);
            return ApiResult.success(null);
        } catch (IllegalArgumentException ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }

    /**
     * 分页查询群发任务列表。
     */
    @ApiOperation("获取群发任务列表")
    @GetMapping("/tasks")
    public ApiResult<List<MassTask>> getMassTaskList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        int offset = (pageNum - 1) * pageSize;
        List<MassTask> tasks = massTaskService.getMassTaskList(offset, pageSize);
        int total = massTaskService.countMassTasks();

        return ApiResult.success(tasks, total);
    }

    /**
     * 查询单个群发任务及其统计信息。
     */
    @ApiOperation("获取群发任务详情")
    @GetMapping("/task/{taskId}")
    public ApiResult<MassTask> getMassTaskById(@PathVariable Long taskId) {
        MassTask task = massTaskService.getMassTaskById(taskId);
        return ApiResult.success(task);
    }

    /**
     * 查询任务下每个接收对象的发送明细。
     */
    @ApiOperation("获取群发任务明细")
    @GetMapping("/task-details/{taskId}")
    public ApiResult<List<MassTaskDetail>> getMassTaskDetails(@PathVariable Long taskId) {
        List<MassTaskDetail> details = massTaskDetailService.getDetailsByTaskId(taskId);
        return ApiResult.success(details);
    }

    /**
     * 取消群发任务。
     */
    @ApiOperation("取消群发任务")
    @PutMapping("/cancel-task/{taskId}")
    public ApiResult<Void> cancelMassTask(@PathVariable Long taskId) {
        massTaskService.cancelMassTask(taskId);
        return ApiResult.success(null);
    }

    /**
     * 保存自定义群发任务，并按接收对象展开明细记录。
     */
    @ApiOperation("创建群发任务")
    @PostMapping("/task")
    public ApiResult<Long> createMassTask(@RequestBody MassTask massTask,
                                          @RequestParam List<Long> receiverIds,
                                          @RequestParam Integer receiverType) {
        List<MassTaskDetail> details = new java.util.ArrayList<>();

        for (Long receiverId : receiverIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(receiverType);
            detail.setReceiverId(receiverId);
            detail.setReceiverName(getReceiverName(receiverType, receiverId));
            detail.setIsSent(MassTaskDetailSentFlagEnum.UNSENT.getCode());
            detail.setSendStatus(MassTaskDetailSendStatusEnum.UNSUCCESSFUL.getCode());
            detail.setCreateTime(java.time.LocalDateTime.now());
            details.add(detail);
        }

        Long taskId = massTaskService.createMassTask(massTask, details);
        return ApiResult.success(taskId);
    }

    /**
     * 根据接收对象类型解析明细中展示的名称。
     */
    private String getReceiverName(Integer receiverType, Long receiverId) {
        if (MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode().equals(receiverType)) {
            WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(receiverId);
            if (friendInfo == null) {
                return "未知用户";
            }
            if (friendInfo.getRealname() != null && !friendInfo.getRealname().trim().isEmpty()) {
                return friendInfo.getRealname();
            }
            return friendInfo.getNickname() != null && !friendInfo.getNickname().trim().isEmpty()
                    ? friendInfo.getNickname()
                    : "未知用户";
        } else if (MassTaskReceiverTypeEnum.GROUP_CHAT.getCode().equals(receiverType)) {
            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(receiverId);
            return groupInfo != null ? groupInfo.getNickname() : "未知群聊";
        } else {
            return "未知接收者";
        }
    }

    /**
     * 更新任务的可编辑字段，不覆盖发送状态和统计字段。
     */
    @ApiOperation("更新群发任务")
    @PutMapping("/task/{taskId}")
    public ApiResult<Void> updateMassTask(@PathVariable Long taskId, @RequestBody MassTask massTask) {
        MassTask existingTask = massTaskService.getMassTaskById(taskId);
        if (existingTask == null) {
            return ApiResult.fail("群发任务不存在");
        }

        existingTask.setTaskName(massTask.getTaskName());
        existingTask.setTaskType(massTask.getTaskType());
        existingTask.setContent(massTask.getContent());
        existingTask.setImageMediaId(massTask.getImageMediaId());
        existingTask.setFileMediaId(massTask.getFileMediaId());
        existingTask.setAudioMediaId(massTask.getAudioMediaId());
        existingTask.setVideoMediaId(massTask.getVideoMediaId());
        existingTask.setPayloadVersion(massTask.getPayloadVersion());
        existingTask.setPayloadJson(massTask.getPayloadJson());
        existingTask.setMsgType(massTask.getMsgType());
        existingTask.setSendTime(massTask.getSendTime());
        existingTask.setCreator(massTask.getCreator());
        existingTask.setRemark(massTask.getRemark());
        existingTask.setTemplateId(massTask.getTemplateId());

        massTaskService.updateById(existingTask);
        return ApiResult.success(null);
    }

    /**
     * 删除任务及其关联的发送明细。
     */
    @ApiOperation("删除群发任务")
    @DeleteMapping("/task/{taskId}")
    public ApiResult<Void> deleteMassTask(@PathVariable Long taskId) {
        massTaskDetailService.deleteByTaskId(taskId);
        massTaskService.removeById(taskId);
        return ApiResult.success(null);
    }

    /**
     * 查询任务统计信息。
     */
    @ApiOperation("获取任务统计信息")
    @GetMapping("/task-stats/{taskId}")
    public ApiResult<MassTask> getTaskStats(@PathVariable Long taskId) {
        MassTask task = massTaskService.getMassTaskById(taskId);
        return ApiResult.success(task);
    }
}
