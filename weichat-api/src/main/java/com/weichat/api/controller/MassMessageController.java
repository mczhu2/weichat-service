package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.GroupMassMessageService;
import com.weichat.api.service.mass.MassMessageTypeSpecService;
import com.weichat.api.service.mass.MassTaskRequestValidator;
import com.weichat.api.vo.request.mass.MassTaskCreateRequest;
import com.weichat.api.vo.response.mass.MassMessageTypeSpecResponse;
import com.weichat.api.vo.response.mass.MassTaskValidationResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.MassTaskDetailService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Api(tags = "Mass Message")
@RestController
@RequestMapping("/api/v1/mass")
public class MassMessageController {

    @Autowired
    private GroupMassMessageService groupMassMessageService;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired
    private MassMessageTypeSpecService massMessageTypeSpecService;

    @Autowired
    private MassTaskRequestValidator massTaskRequestValidator;

    @ApiOperation("Create text task for users")
    @PostMapping("/send-to-users")
    public ApiResult<Long> sendMassMessageToUsers(@RequestParam String uuid,
                                                  @RequestParam List<Long> userIds,
                                                  @RequestParam String content) {
        Long taskId = groupMassMessageService.sendMassMessageToUsers(uuid, userIds, content);
        return ApiResult.success(taskId);
    }

    @ApiOperation("Create template task for users")
    @PostMapping("/send-to-users-with-template")
    public ApiResult<Long> sendMassMessageToUsersWithTemplate(@RequestParam String uuid,
                                                              @RequestParam List<Long> userIds,
                                                              @RequestParam Long templateId,
                                                              @RequestBody(required = false) Map<String, Object> variables) {
        Long taskId = groupMassMessageService.sendMassMessageToUsersWithTemplate(uuid, userIds, templateId, variables);
        return ApiResult.success(taskId);
    }

    @ApiOperation("Create text task for groups")
    @PostMapping("/send-to-groups")
    public ApiResult<Long> sendMassMessageToGroups(@RequestParam String uuid,
                                                   @RequestParam List<Long> groupIds,
                                                   @RequestParam String content) {
        Long taskId = groupMassMessageService.sendMassMessageToGroups(uuid, groupIds, content);
        return ApiResult.success(taskId);
    }

    @ApiOperation("Create template task for groups")
    @PostMapping("/send-to-groups-with-template")
    public ApiResult<Long> sendMassMessageToGroupsWithTemplate(@RequestParam String uuid,
                                                               @RequestParam List<Long> groupIds,
                                                               @RequestParam Long templateId,
                                                               @RequestBody(required = false) Map<String, Object> variables) {
        Long taskId = groupMassMessageService.sendMassMessageToGroupsWithTemplate(uuid, groupIds, templateId, variables);
        return ApiResult.success(taskId);
    }

    @ApiOperation("Trigger task manually")
    @PostMapping("/trigger-task/{taskId}")
    public ApiResult<Boolean> triggerMassTask(@PathVariable Long taskId) {
        boolean result = groupMassMessageService.triggerMassTask(taskId);
        return ApiResult.success(result);
    }

    @ApiOperation("List tasks")
    @GetMapping("/tasks")
    public ApiResult<List<MassTask>> getMassTaskList(@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return ApiResult.success(massTaskService.getMassTaskList(offset, pageSize));
    }

    @ApiOperation("Get message type specs")
    @GetMapping("/message-type-specs")
    public ApiResult<List<MassMessageTypeSpecResponse>> getMessageTypeSpecs() {
        return ApiResult.success(massMessageTypeSpecService.getAllSpecs());
    }

    @ApiOperation("Validate rich task request")
    @PostMapping("/task/validate")
    public ApiResult<MassTaskValidationResponse> validateMassTask(@RequestBody MassTaskCreateRequest request) {
        List<String> errors = massTaskRequestValidator.validateCreateRequest(request);
        MassTaskValidationResponse response = new MassTaskValidationResponse();
        response.setValid(errors.isEmpty());
        response.setErrors(errors);
        return ApiResult.success(response);
    }

    @ApiOperation("Get task detail")
    @GetMapping("/task/{taskId}")
    public ApiResult<MassTask> getMassTaskById(@PathVariable Long taskId) {
        return ApiResult.success(massTaskService.getMassTaskById(taskId));
    }

    @ApiOperation("Get task detail items")
    @GetMapping("/task-details/{taskId}")
    public ApiResult<List<MassTaskDetail>> getMassTaskDetails(@PathVariable Long taskId) {
        return ApiResult.success(massTaskDetailService.getDetailsByTaskId(taskId));
    }

    @ApiOperation("Cancel task")
    @PutMapping("/cancel-task/{taskId}")
    public ApiResult<Void> cancelMassTask(@PathVariable Long taskId) {
        massTaskService.cancelMassTask(taskId);
        return ApiResult.success(null);
    }

    @ApiOperation("Create task by legacy payload")
    @PostMapping("/task")
    public ApiResult<Long> createMassTask(@RequestBody MassTask massTask,
                                          @RequestParam List<Long> receiverIds,
                                          @RequestParam Integer receiverType) {
        List<String> errors = massTaskRequestValidator.validateLegacyTask(massTask, receiverIds, receiverType);
        if (!errors.isEmpty()) {
            return ApiResult.fail(String.join("; ", errors));
        }

        List<MassTaskDetail> details = new ArrayList<>();
        for (Long receiverId : receiverIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(receiverType);
            detail.setReceiverId(receiverId);
            detail.setReceiverName(getReceiverName(receiverType, receiverId));
            detail.setIsSent(0);
            detail.setSendStatus(0);
            detail.setCreateTime(LocalDateTime.now());
            details.add(detail);
        }

        Long taskId = massTaskService.createMassTask(massTask, details);
        return ApiResult.success(taskId);
    }

    private String getReceiverName(Integer receiverType, Long receiverId) {
        if (receiverType == 1) {
            WxUserInfo userInfo = wxUserInfoService.selectByPrimaryKey(receiverId);
            return userInfo != null ? userInfo.getRealname() : "unknown-user";
        }
        if (receiverType == 2) {
            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(receiverId);
            return groupInfo != null ? groupInfo.getNickname() : "unknown-group";
        }
        return "unknown-receiver";
    }

    @ApiOperation("Update legacy task")
    @PutMapping("/task/{taskId}")
    public ApiResult<Void> updateMassTask(@PathVariable Long taskId, @RequestBody MassTask massTask) {
        MassTask existingTask = massTaskService.getMassTaskById(taskId);
        if (existingTask == null) {
            return ApiResult.fail("mass task not found");
        }

        List<String> errors = massTaskRequestValidator.validateLegacyTask(
                massTask,
                Collections.singletonList(1L),
                massTask.getTaskType()
        );
        errors.remove("receiverIds must not be empty");
        if (!errors.isEmpty()) {
            return ApiResult.fail(String.join("; ", errors));
        }

        existingTask.setTaskName(massTask.getTaskName());
        existingTask.setTaskType(massTask.getTaskType());
        existingTask.setContent(massTask.getContent());
        existingTask.setImageMediaId(massTask.getImageMediaId());
        existingTask.setFileMediaId(massTask.getFileMediaId());
        existingTask.setAudioMediaId(massTask.getAudioMediaId());
        existingTask.setVideoMediaId(massTask.getVideoMediaId());
        existingTask.setMsgType(massTask.getMsgType());
        existingTask.setSendTime(massTask.getSendTime());
        existingTask.setCreator(massTask.getCreator());
        existingTask.setRemark(massTask.getRemark());
        existingTask.setTemplateId(massTask.getTemplateId());

        massTaskService.updateById(existingTask);
        return ApiResult.success(null);
    }

    @ApiOperation("Delete task")
    @DeleteMapping("/task/{taskId}")
    public ApiResult<Void> deleteMassTask(@PathVariable Long taskId) {
        massTaskDetailService.deleteByTaskId(taskId);
        massTaskService.removeById(taskId);
        return ApiResult.success(null);
    }

    @ApiOperation("Get task stats")
    @GetMapping("/task-stats/{taskId}")
    public ApiResult<MassTask> getTaskStats(@PathVariable Long taskId) {
        return ApiResult.success(massTaskService.getMassTaskById(taskId));
    }
}
