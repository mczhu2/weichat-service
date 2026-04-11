package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息模板实体类
 *
 * @author weichat
 */
@Data
public class MessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 模板变量定义(JSON格式)
     */
    private String variables;

    /**
     * 消息类型 (0-文本, 1-图片, 2-文件, 3-音频, 4-视频)
     */
    private Integer msgType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String creator;
}