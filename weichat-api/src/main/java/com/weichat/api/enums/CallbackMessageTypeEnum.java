package com.weichat.api.enums;

import lombok.Getter;

@Getter
public enum CallbackMessageTypeEnum {

    UNKNOWN(0),
    TEXT(1),
    VOICE(16),
    IMAGE(101),
    VIDEO(103);

    private final int code;

    CallbackMessageTypeEnum(int code) {
        this.code = code;
    }

    public static CallbackMessageTypeEnum fromCode(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (CallbackMessageTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
