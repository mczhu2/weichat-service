package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.service.MessageTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息模板管理控制器。
 */
@Api(tags = "消息模板")
@RestController
@RequestMapping("/api/v1/message-template")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    /**
     * 创建消息模板。
     */
    @ApiOperation("创建消息模板")
    @PostMapping
    public ApiResult<Long> createTemplate(@RequestBody MessageTemplate template) {
        Long templateId = messageTemplateService.createTemplate(template);
        return ApiResult.success(templateId);
    }

    /**
     * 更新指定消息模板。
     */
    @ApiOperation("更新消息模板")
    @PutMapping("/{templateId}")
    public ApiResult<Void> updateTemplate(@PathVariable Long templateId, @RequestBody MessageTemplate template) {
        template.setId(templateId);
        messageTemplateService.updateTemplate(template);
        return ApiResult.success(null);
    }

    /**
     * 删除指定消息模板。
     */
    @ApiOperation("删除消息模板")
    @DeleteMapping("/{templateId}")
    public ApiResult<Void> deleteTemplate(@PathVariable Long templateId) {
        messageTemplateService.deleteTemplate(templateId);
        return ApiResult.success(null);
    }

    /**
     * 查询单个模板详情。
     */
    @ApiOperation("获取消息模板详情")
    @GetMapping("/{templateId}")
    public ApiResult<MessageTemplate> getTemplate(@PathVariable Long templateId) {
        MessageTemplate template = messageTemplateService.getTemplateById(templateId);
        return ApiResult.success(template);
    }

    /**
     * 分页查询模板列表。
     */
    @ApiOperation("获取消息模板列表")
    @GetMapping
    public ApiResult<List<MessageTemplate>> getTemplateList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        int offset = (pageNum - 1) * pageSize;
        List<MessageTemplate> templates = messageTemplateService.getTemplatesByOffset(offset, pageSize);

        return ApiResult.success(templates);
    }
}
