package com.weichat.api.vo.callback;

import lombok.Data;

/**
 * 下游业务回调里的结构化媒体信息。
 */
@Data
public class DownstreamMediaVo {

    /**
     * 媒体类型：image / video / voice。
     */
    private String mediaType;

    /**
     * 可直接访问的媒体 URL。
     * 如果下载临时地址失败，该字段允许为空，不影响主流程。
     */
    private String mediaUrl;

    /**
     * 原始媒资标识。
     * 当 mediaUrl 获取失败时，下游仍可拿到原始文件标识做兼容处理。
     */
    private String rawMediaId;

    /**
     * 文件名。
     */
    private String fileName;

    /**
     * 文件大小，单位字节。
     */
    private Integer size;

    /**
     * 媒体时长，语音/视频消息使用秒数。
     */
    private Integer duration;

    /**
     * 图片/视频宽度。
     */
    private Integer width;

    /**
     * 图片/视频高度。
     */
    private Integer height;

    /**
     * 视频预览图地址。
     */
    private String previewUrl;
}
