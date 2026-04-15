package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String templateName;

    private Integer msgType;

    private String templateContent;

    private String payloadVersion;

    private String payloadJson;

    private String variables;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String creator;
}
