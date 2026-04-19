package com.weichat.common.enums;

import lombok.Getter;

@Getter
public enum MassTaskPlanScheduleTypeEnum {

    ONCE(1, "once"),
    DAILY(2, "daily"),
    WEEKLY(3, "weekly"),
    MONTHLY(4, "monthly");

    private final Integer code;
    private final String description;

    MassTaskPlanScheduleTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static boolean contains(Integer code) {
        if (code == null) {
            return false;
        }
        for (MassTaskPlanScheduleTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
