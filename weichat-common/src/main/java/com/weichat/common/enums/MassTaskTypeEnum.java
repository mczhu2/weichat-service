package com.weichat.common.enums;

import lombok.Getter;

/**
 * 群发任务类型。
 */
@Getter
public enum MassTaskTypeEnum {

    USER(1, "好友群发"),
    GROUP(2, "群群发");

    private final Integer code;
    private final String description;

    MassTaskTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
