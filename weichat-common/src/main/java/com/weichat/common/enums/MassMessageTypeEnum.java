package com.weichat.common.enums;

import lombok.Getter;

@Getter
public enum MassMessageTypeEnum {

    TEXT(0, "text"),
    IMAGE(1, "image"),
    FILE(2, "file"),
    AUDIO(3, "voice"),
    VIDEO(4, "video"),
    LINK(5, "link"),
    APP(6, "app"),
    COMPOSITE(7, "composite");

    private final Integer code;
    private final String description;

    MassMessageTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
