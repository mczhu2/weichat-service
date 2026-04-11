package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 群发任务主表
 *
 * @author weichat
 */
@Data
public class MassTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型 (1-好友群发, 2-群群发)
     */
    private Integer taskType;

    /**
     * 任务内容 (文本内容)
     */
    private String content;

    /**
     * 图片素材ID
     */
    private String imageMediaId;

    /**
     * 文件素材ID
     */
    private String fileMediaId;

    /**
     * 音频素材ID
     */
    private String audioMediaId;

    /**
     * 视频素材ID
     */
    private String videoMediaId;

    /**
     * 消息类型 (0-文本, 1-图片, 2-文件, 3-音频, 4-视频)
     */
    private Integer msgType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送状态 (0-未发送, 1-发送中, 2-已完成, 3-已取消)
     */
    private Integer sendStatus;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 总数
     */
    private Integer totalCount;

    /**
     * 已发送数量
     */
    private Integer sentCount;

    /**
     * 发送成功的数量
     */
    private Integer successCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模板ID
     */
    private Long templateId;
}