package com.weichat.common.enums;

import lombok.Getter;

/**
 * 群发任务发送状态。
 */
@Getter
public enum MassTaskSendStatusEnum {

    PENDING(0, "未发送"),
    SENDING(1, "发送中"),
    COMPLETED(2, "已完成"),
    CANCELLED(3, "已取消");

    private final Integer code;
    private final String description;

    MassTaskSendStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
