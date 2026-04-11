package com.weichat.common.service;

import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息模板服务，负责模板的增删改查。
 */
@Service
public class MessageTemplateService {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    /**
     * 创建消息模板。
     */
    public Long createTemplate(MessageTemplate template) {
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        messageTemplateMapper.insert(template);
        return template.getId();
    }

    /**
     * 更新消息模板。
     */
    public void updateTemplate(MessageTemplate template) {
        template.setUpdateTime(LocalDateTime.now());
        messageTemplateMapper.updateByPrimaryKey(template);
    }

    /**
     * 删除消息模板。
     */
    public void deleteTemplate(Long templateId) {
        messageTemplateMapper.deleteByPrimaryKey(templateId);
    }

    /**
     * 根据模板 ID 查询模板详情。
     */
    public MessageTemplate getTemplateById(Long templateId) {
        return messageTemplateMapper.selectByPrimaryKey(templateId);
    }

    /**
     * 查询全部消息模板。
     */
    public List<MessageTemplate> getAllTemplates() {
        return messageTemplateMapper.selectAll();
    }

    /**
     * 根据创建人查询模板列表。
     */
    public List<MessageTemplate> getTemplatesByCreator(String creator) {
        return messageTemplateMapper.selectByCreator(creator);
    }

    /**
     * 分页查询模板列表。
     */
    public List<MessageTemplate> getTemplatesByOffset(int offset, int limit) {
        return messageTemplateMapper.selectWithPaging(offset, limit);
    }
}
