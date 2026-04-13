package com.weichat.common.enums;

import lombok.Getter;

/**
 * 群发明细发送结果状态。
 */
@Getter
public enum MassTaskDetailSendStatusEnum {

    UNSUCCESSFUL(0, "未成功"),
    SUCCESS(1, "成功");

    private final Integer code;
    private final String description;

    MassTaskDetailSendStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
