package com.weichat.common.enums;

import lombok.Getter;

@Getter
public enum MassTaskPlanStatusEnum {

    ENABLED(1, "enabled"),
    PAUSED(2, "paused"),
    CANCELLED(3, "cancelled"),
    FINISHED(4, "finished");

    private final Integer code;
    private final String description;

    MassTaskPlanStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static boolean contains(Integer code) {
        if (code == null) {
            return false;
        }
        for (MassTaskPlanStatusEnum value : values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
