package com.weichat.api.enums;

/**
 * 朋友圈文件类型枚举
 *
 * @author weichat
 * @since 1.0
 */
public enum PyqFileType {

    /**
     * 图片
     */
    IMAGE(1, "图片"),

    /**
     * 视频
     */
    VIDEO(2, "视频"),

    /**
     * 视频封面图
     */
    VIDEO_COVER(3, "视频封面图");

    private final Integer code;
    private final String description;

    PyqFileType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 类型代码
     * @return 枚举对象，若不存在则返回null
     */
    public static PyqFileType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PyqFileType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为图片类型（包括普通图片和视频封面图）
     *
     * @return true-是图片类型，false-不是
     */
    public boolean isImageType() {
        return this == IMAGE || this == VIDEO_COVER;
    }

    /**
     * 判断是否为视频类型
     *
     * @return true-是视频类型，false-不是
     */
    public boolean isVideoType() {
        return this == VIDEO;
    }
}
