package com.weichat.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageTemplateService {

    private static final int MIN_MSG_TYPE = 0;
    private static final int MAX_MSG_TYPE = 7;
    private static final int MAX_SINGLE_MSG_TYPE = 6;
    private static final int COMPOSITE_MSG_TYPE = 7;
    private static final String DEFAULT_PAYLOAD_VERSION = "v1";

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    public Long createTemplate(MessageTemplate template) {
        validateTemplate(template, false);
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        messageTemplateMapper.insert(template);
        return template.getId();
    }

    public void updateTemplate(MessageTemplate template) {
        validateTemplate(template, true);
        template.setUpdateTime(LocalDateTime.now());
        messageTemplateMapper.updateByPrimaryKey(template);
    }

    public void deleteTemplate(Long templateId) {
        messageTemplateMapper.deleteByPrimaryKey(templateId);
    }

    public MessageTemplate getTemplateById(Long templateId) {
        return messageTemplateMapper.selectByPrimaryKey(templateId);
    }

    public List<MessageTemplate> getAllTemplates() {
        return messageTemplateMapper.selectAll();
    }

    public List<MessageTemplate> getTemplatesByCreator(String creator) {
        return messageTemplateMapper.selectByCreator(creator);
    }

    public List<MessageTemplate> getTemplatesByOffset(int offset, int limit) {
        return messageTemplateMapper.selectWithPaging(offset, limit);
    }

    /**
     * 校验模板基础字段，并对组合模板做额外格式校验。
     *
     * @param template 待校验的模板对象
     * @param update   是否为更新场景
     */
    private void validateTemplate(MessageTemplate template, boolean update) {
        if (template == null) {
            throw new IllegalArgumentException("message template is required");
        }
        if (update && template.getId() == null) {
            throw new IllegalArgumentException("template id is required");
        }
        if (!StringUtils.hasText(template.getTemplateName())) {
            throw new IllegalArgumentException("templateName is required");
        }
        if (!isSupportedMsgType(template.getMsgType())) {
            throw new IllegalArgumentException("msgType is invalid");
        }
        if (!StringUtils.hasText(template.getPayloadVersion())) {
            template.setPayloadVersion(DEFAULT_PAYLOAD_VERSION);
        }

        if (COMPOSITE_MSG_TYPE == template.getMsgType()) {
            validateCompositeTemplateContent(template.getTemplateContent());
            return;
        }

        if (!StringUtils.hasText(template.getTemplateContent())
                && !StringUtils.hasText(template.getPayloadJson())) {
            throw new IllegalArgumentException("templateContent or payloadJson is required");
        }
    }

    /**
     * 判断消息类型是否在当前模板支持范围内。
     *
     * @param msgType 消息类型
     * @return true 表示支持，false 表示不支持
     */
    private boolean isSupportedMsgType(Integer msgType) {
        return msgType != null && msgType >= MIN_MSG_TYPE && msgType <= MAX_MSG_TYPE;
    }

    /**
     * 校验组合模板内容是否为合法的 JSON 数组，并校验每个子消息结构。
     *
     * @param templateContent 组合模板内容
     */
    private void validateCompositeTemplateContent(String templateContent) {
        if (!StringUtils.hasText(templateContent)) {
            throw new IllegalArgumentException("templateContent is required when msgType=7");
        }

        JSONArray components;
        try {
            components = JSON.parseArray(templateContent);
        } catch (Exception ex) {
            throw new IllegalArgumentException("templateContent must be a JSON array when msgType=7", ex);
        }

        if (components == null || components.isEmpty()) {
            throw new IllegalArgumentException("templateContent must contain at least one component when msgType=7");
        }

        for (int i = 0; i < components.size(); i++) {
            JSONObject component = toJsonObject(components.get(i), i);
            Integer childMsgType = component.getInteger("msgType");
            if (childMsgType == null || childMsgType < MIN_MSG_TYPE || childMsgType > MAX_SINGLE_MSG_TYPE) {
                throw new IllegalArgumentException("component[" + i + "].msgType must be between 0 and 6");
            }

            Object childTemplateContent = component.get("templateContent");
            if (childTemplateContent == null || !StringUtils.hasText(String.valueOf(childTemplateContent))) {
                throw new IllegalArgumentException("component[" + i + "].templateContent is required");
            }
        }
    }

    /**
     * 将组合模板中的单个元素安全转换为 JSON 对象，便于后续读取字段。
     *
     * @param value 组合项原始值
     * @param index 组合项下标
     * @return 转换后的 JSON 对象
     */
    private JSONObject toJsonObject(Object value, int index) {
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        if (value == null) {
            throw new IllegalArgumentException("component[" + index + "] is required");
        }
        try {
            return JSON.parseObject(JSON.toJSONString(value));
        } catch (Exception ex) {
            throw new IllegalArgumentException("component[" + index + "] must be a JSON object", ex);
        }
    }
}
