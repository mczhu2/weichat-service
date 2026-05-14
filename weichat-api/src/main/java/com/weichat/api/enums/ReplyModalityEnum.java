package com.weichat.api.enums;

import lombok.Getter;

@Getter
public enum ReplyModalityEnum {

    TEXT("text"),
    AUDIO("audio");

    private final String code;

    ReplyModalityEnum(String code) {
        this.code = code;
    }
}
