package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.GroupMassMessageService;
import com.weichat.api.service.MassMessageService;
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

import java.util.List;
import java.util.Map;

/**
 * 群发功能控制器
 *
 * @author weichat
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
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    /**
     * 向多个用户群发消息
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
     * 向多个用户群发消息（使用模板）
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
     * 向多个群聊群发消息
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
     * 向多个群聊群发消息（使用模板）
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
     * 手动触发群发任务
     */
    @ApiOperation("手动触发群发任务")
    @PostMapping("/trigger-task/{taskId}")
    public ApiResult<Boolean> triggerMassTask(@PathVariable Long taskId) {
        boolean result = groupMassMessageService.triggerMassTask(taskId);
        return ApiResult.success(result);
    }

    /**
     * 获取群发任务列表
     */
    @ApiOperation("获取群发任务列表")
    @GetMapping("/tasks")
    public ApiResult<List<MassTask>> getMassTaskList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        List<MassTask> tasks = massTaskService.getMassTaskList(offset, pageSize);

        return ApiResult.success(tasks);
    }

    /**
     * 获取群发任务详情
     */
    @ApiOperation("获取群发任务详情")
    @GetMapping("/task/{taskId}")
    public ApiResult<MassTask> getMassTaskById(@PathVariable Long taskId) {
        MassTask task = massTaskService.getMassTaskById(taskId);
        return ApiResult.success(task);
    }

    /**
     * 获取群发任务明细
     */
    @ApiOperation("获取群发任务明细")
    @GetMapping("/task-details/{taskId}")
    public ApiResult<List<MassTaskDetail>> getMassTaskDetails(@PathVariable Long taskId) {
        List<MassTaskDetail> details = massTaskDetailService.getDetailsByTaskId(taskId);
        return ApiResult.success(details);
    }

    /**
     * 取消群发任务
     */
    @ApiOperation("取消群发任务")
    @PutMapping("/cancel-task/{taskId}")
    public ApiResult<Void> cancelMassTask(@PathVariable Long taskId) {
        massTaskService.cancelMassTask(taskId);
        return ApiResult.success(null);
    }

    /**
     * 创建群发任务
     */
    @ApiOperation("创建群发任务")
    @PostMapping("/task")
    public ApiResult<Long> createMassTask(@RequestBody MassTask massTask,
                                          @RequestParam List<Long> receiverIds,
                                          @RequestParam Integer receiverType) {
        // 创建任务明细
        List<MassTaskDetail> details = new java.util.ArrayList<>();
        
        for (Long receiverId : receiverIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(receiverType); // 1-外部联系人, 2-群聊
            detail.setReceiverId(receiverId);
            
            // 根据接收者类型和ID获取接收者名称
            String receiverName = getReceiverName(receiverType, receiverId);
            detail.setReceiverName(receiverName);
            
            detail.setIsSent(0); // 未发送
            detail.setSendStatus(0); // 默认失败状态
            detail.setCreateTime(java.time.LocalDateTime.now());
            details.add(detail);
        }

        Long taskId = massTaskService.createMassTask(massTask, details);
        return ApiResult.success(taskId);
    }

    /**
     * 根据接收者类型和ID获取接收者名称
     * @param receiverType 接收者类型 (1-外部联系人, 2-群聊)
     * @param receiverId 接收者ID
     * @return 接收者名称
     */
    private String getReceiverName(Integer receiverType, Long receiverId) {
        if (receiverType == 1) { // 外部联系人
            WxUserInfo userInfo = wxUserInfoService.selectByPrimaryKey(receiverId);
            return userInfo != null ? userInfo.getRealname() : "未知用户";
        } else if (receiverType == 2) { // 群聊
            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(receiverId);
            return groupInfo != null ? groupInfo.getNickname() : "未知群聊";
        } else {
            return "未知接收者";
        }
    }

    /**
     * 更新群发任务
     */
    @ApiOperation("更新群发任务")
    @PutMapping("/task/{taskId}")
    public ApiResult<Void> updateMassTask(@PathVariable Long taskId, @RequestBody MassTask massTask) {
        // 从数据库获取原任务数据
        MassTask existingTask = massTaskService.getMassTaskById(taskId);
        if (existingTask == null) {
            return ApiResult.fail("群发任务不存在");
        }

        // 只更新允许更新的字段，避免更新关键字段如状态、统计信息等
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

    /**
     * 删除群发任务
     */
    @ApiOperation("删除群发任务")
    @DeleteMapping("/task/{taskId}")
    public ApiResult<Void> deleteMassTask(@PathVariable Long taskId) {
        // 同时删除关联的明细记录
        massTaskDetailService.deleteByTaskId(taskId);
        massTaskService.removeById(taskId);
        return ApiResult.success(null);
    }

    /**
     * 获取任务统计信息
     */
    @ApiOperation("获取任务统计信息")
    @GetMapping("/task-stats/{taskId}")
    public ApiResult<MassTask> getTaskStats(@PathVariable Long taskId) {
        MassTask task = massTaskService.getMassTaskById(taskId);
        return ApiResult.success(task);
    }
}