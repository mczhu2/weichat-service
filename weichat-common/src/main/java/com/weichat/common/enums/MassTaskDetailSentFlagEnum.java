package com.weichat.common.enums;

import lombok.Getter;

/**
 * 群发明细是否已发送标记。
 */
@Getter
public enum MassTaskDetailSentFlagEnum {

    UNSENT(0, "未发送"),
    SENT(1, "已发送");

    private final Integer code;
    private final String description;

    MassTaskDetailSentFlagEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
