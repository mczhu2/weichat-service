package com.weichat.common.enums;

import lombok.Getter;

/**
 * 群发任务接收方类型。
 */
@Getter
public enum MassTaskReceiverTypeEnum {

    EXTERNAL_CONTACT(1, "外部联系人"),
    GROUP_CHAT(2, "群聊");

    private final Integer code;
    private final String description;

    MassTaskReceiverTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
