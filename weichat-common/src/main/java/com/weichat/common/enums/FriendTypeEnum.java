package com.weichat.common.enums;

/**
 * 好友类型枚举
 *
 * @author weichat
 */
public enum FriendTypeEnum {
    
    /**
     * 企业微信好友
     */
    ENTERPRISE(0, "企业微信好友"),
    
    /**
     * 外部微信好友
     */
    EXTERNAL(1, "外部微信好友");
    
    private final Integer code;
    private final String desc;
    
    FriendTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    /**
     * 根据code获取枚举
     * @param code 编码
     * @return 枚举
     */
    public static FriendTypeEnum getByCode(Integer code) {
        for (FriendTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
