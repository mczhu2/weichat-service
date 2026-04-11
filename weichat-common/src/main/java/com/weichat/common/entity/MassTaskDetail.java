package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 群发任务明细表
 *
 * @author weichat
 */
@Data
public class MassTaskDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的群发任务ID
     */
    private Long taskId;

    /**
     * 接收方类型 (1-外部联系人, 2-群聊)
     */
    private Integer receiverType;

    /**
     * 接收方ID (用户ID或群ID)
     */
    private Long receiverId;

    /**
     * 接收方名称
     */
    private String receiverName;

    /**
     * 是否已发送 (0-未发送, 1-已发送)
     */
    private Integer isSent;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送状态 (0-失败, 1-成功)
     */
    private Integer sendStatus;

    /**
     * 发送结果描述
     */
    private String sendResult;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}