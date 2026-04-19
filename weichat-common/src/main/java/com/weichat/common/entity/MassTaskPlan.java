package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 群发计划表，负责描述“何时生成一次执行任务”。
 */
@Data
public class MassTaskPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 计划名称 */
    private String planName;

    /** 任务类型：1-好友，2-群聊 */
    private Integer taskType;

    /** 接收方类型：1-好友，2-群聊 */
    private Integer receiverType;

    /** 接收方ID集合JSON */
    private String receiverIdsJson;

    /** 消息类型 */
    private Integer msgType;

    /** payload版本 */
    private String payloadVersion;

    /** 富消息载荷JSON */
    private String payloadJson;

    /** 模板ID */
    private Long templateId;

    /** 创建人或设备uuid */
    private String creator;

    /** 备注 */
    private String remark;

    /** 周期类型：1-单次，2-每天，3-每周，4-每月 */
    private Integer scheduleType;

    /** 周期规则JSON，例如 weekdays/monthDays */
    private String scheduleRuleJson;

    /** 生效开始时间 */
    private LocalDateTime effectiveStartAt;

    /** 生效结束时间 */
    private LocalDateTime effectiveEndAt;

    /** 每日发送窗口开始时间，HH:mm:ss */
    private String windowStartTime;

    /** 每日发送窗口结束时间，HH:mm:ss */
    private String windowEndTime;

    /** 每分钟允许发送的最大接收人数 */
    private Integer ratePerMinute;

    /** 时区 */
    private String timezone;

    /** 下一次需要生成执行任务的时间 */
    private LocalDateTime nextTriggerTime;

    /** 最近一次已生成执行任务的时间 */
    private LocalDateTime lastTriggerTime;

    /** 计划状态 */
    private Integer planStatus;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
