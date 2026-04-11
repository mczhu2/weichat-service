package com.weichat.common.service;

import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息模板服务类
 *
 * @author weichat
 */
@Service
public class MessageTemplateService {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    /**
     * 创建消息模板
     *
     * @param template 消息模板对象
     * @return 模板ID
     */
    public Long createTemplate(MessageTemplate template) {
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        messageTemplateMapper.insert(template);
        return template.getId();
    }

    /**
     * 更新消息模板
     *
     * @param template 消息模板对象
     */
    public void updateTemplate(MessageTemplate template) {
        template.setUpdateTime(LocalDateTime.now());
        messageTemplateMapper.updateByPrimaryKey(template);
    }

    /**
     * 删除消息模板
     *
     * @param templateId 模板ID
     */
    public void deleteTemplate(Long templateId) {
        messageTemplateMapper.deleteByPrimaryKey(templateId);
    }

    /**
     * 根据ID获取消息模板
     *
     * @param templateId 模板ID
     * @return 消息模板对象
     */
    public MessageTemplate getTemplateById(Long templateId) {
        return messageTemplateMapper.selectByPrimaryKey(templateId);
    }

    /**
     * 获取所有消息模板
     *
     * @return 消息模板列表
     */
    public List<MessageTemplate> getAllTemplates() {
        return messageTemplateMapper.selectAll();
    }

    /**
     * 根据创建人获取模板列表
     *
     * @param creator 创建人
     * @return 模板列表
     */
    public List<MessageTemplate> getTemplatesByCreator(String creator) {
        return messageTemplateMapper.selectByCreator(creator);
    }

    /**
     * 根据偏移量和限制数量获取模板列表
     *
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 模板列表
     */
    public List<MessageTemplate> getTemplatesByOffset(int offset, int limit) {
        return messageTemplateMapper.selectWithPaging(offset, limit);
    }
}