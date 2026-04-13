package com.weichat.common.enums;

import lombok.Getter;

/**
 * 群发消息类型。
 */
@Getter
public enum MassMessageTypeEnum {

    TEXT(0, "文本"),
    IMAGE(1, "图片"),
    FILE(2, "文件"),
    AUDIO(3, "音频"),
    VIDEO(4, "视频");

    private final Integer code;
    private final String description;

    MassMessageTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
