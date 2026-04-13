package com.weichat.common.enums;

import lombok.Getter;

@Getter
public enum MassMessageTypeEnum {

    TEXT(0, "文本"),
    IMAGE(1, "图片"),
    FILE(2, "文件"),
    AUDIO(3, "语音"),
    VIDEO(4, "视频"),
    LINK(5, "链接"),
    APP(6, "小程序");

    private final Integer code;
    private final String description;

    MassMessageTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
