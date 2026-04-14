package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.request.mass.MassTaskMediaPayload;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 群发任务媒体素材模型
 * <p>
 * 用于封装群发消息中的媒体文件信息，支持图片、视频、语音等多种媒体类型。
 * 包含媒体文件的基本属性（URL、Base64编码）、文件元数据（大小、MD5）、
 * 以及特定媒体类型的扩展属性（图片尺寸、视频时长、语音时长等）。
 * </p>
 *
 * @author weichat
 * @since 1.0
 */
@Data
public class MassTaskMediaMaterial {

    /**
     * 媒体文件的URL地址
     */
    private String url;

    /**
     * 媒体文件的Base64编码内容
     */
    private String base64;

    /**
     * 文件名（驼峰命名）
     */
    private String filename;

    /**
     * 文件名（下划线命名）
     */
    private String fileName;

    /**
     * 文件MIME类型，如 image/jpeg、video/mp4、audio/amr
     */
    private String contentType;

    /**
     * 微信CDN密钥，用于从微信CDN下载文件
     */
    private String cdnkey;

    /**
     * AES加密密钥，用于解密微信加密的媒体文件
     */
    private String aeskey;

    /**
     * 文件MD5校验值
     */
    private String md5;

    /**
     * 微信文件ID
     */
    private String fileId;

    /**
     * 文件大小（字节）
     */
    private Integer fileSize;

    /**
     * 图片宽度（像素）
     */
    private Integer width;

    /**
     * 图片高度（像素）
     */
    private Integer height;

    /**
     * 缩略图高度（像素）
     */
    private Integer thumbImageHeight;

    /**
     * 缩略图宽度（像素）
     */
    private Integer thumbImageWidth;

    /**
     * 缩略图文件大小（字节）
     */
    private Integer thumbFileSize;

    /**
     * 缩略图文件MD5校验值
     */
    private String thumbFileMd5;

    /**
     * 是否高清图片标识，1-高清，0-普通
     */
    private Integer isHd;

    /**
     * 语音时长（秒）
     */
    private Integer voiceTime;

    /**
     * 视频时长（秒）
     */
    private Integer videoDuration;

    /**
     * 视频宽度（像素）
     */
    private Integer videoWidth;

    /**
     * 视频高度（像素）
     */
    private Integer videoHeight;

    /**
     * 从群发任务媒体载荷对象创建素材实例
     * <p>
     * 将用户提交的群发媒体载荷数据转换为内部素材模型，
     * 主要提取URL、Base64编码、文件名、内容类型等基础信息。
     * </p>
     *
     * @param payload 群发任务媒体载荷对象
     * @return 媒体素材实例
     */
    public static MassTaskMediaMaterial fromPayload(MassTaskMediaPayload payload) {
        MassTaskMediaMaterial material = new MassTaskMediaMaterial();
        material.url = payload.getUrl();
        material.base64 = payload.getBase64();
        material.filename = payload.getFilename();
        material.contentType = payload.getContentType();
        material.voiceTime = payload.getVoiceTime();
        material.isHd = payload.getIsHd();
        return material;
    }

    /**
     * 从JSON对象创建媒体素材实例
     * <p>
     * 支持多种字段命名风格（驼峰、下划线），自动适配不同来源的JSON数据。
     * 主要用于解析微信回调、第三方接口返回的媒体信息。
     * </p>
     *
     * @param json JSON对象，包含媒体文件的各项属性
     * @return 媒体素材实例
     */
    public static MassTaskMediaMaterial fromJson(JSONObject json) {
        MassTaskMediaMaterial material = new MassTaskMediaMaterial();
        material.url = readText(json, "url", "mediaUrl", "media_url");
        material.base64 = readText(json, "base64", "data", "content");
        material.filename = readText(json, "filename", "fileName");
        material.fileName = readText(json, "file_name", "name");
        material.contentType = readText(json, "contentType", "content_type", "mimeType", "mime_type");
        material.cdnkey = readText(json, "cdnkey", "cdn_key", "thumbFileId");
        material.aeskey = readText(json, "aeskey", "aes_key", "thumbAESKey");
        material.md5 = readText(json, "md5", "thumbMD5");
        material.fileId = readText(json, "fileid", "file_id");
        material.fileSize = readInt(json, "fileSize", "file_size", "size");
        material.width = readInt(json, "width", "image_width");
        material.height = readInt(json, "height", "image_height");
        material.thumbImageHeight = readInt(json, "thumb_image_height", "thumbImageHeight");
        material.thumbImageWidth = readInt(json, "thumb_image_width", "thumbImageWidth");
        material.thumbFileSize = readInt(json, "thumb_file_size", "thumbFileSize", "video_img_size");
        material.thumbFileMd5 = readText(json, "thumb_file_md5", "thumbFileMd5");
        material.isHd = readInt(json, "is_hd", "isHd");
        material.voiceTime = readInt(json, "voice_time", "voiceTime", "voice_duration", "duration");
        material.videoDuration = readInt(json, "video_duration", "videoDuration", "duration");
        material.videoWidth = readInt(json, "video_width", "videoWidth", "width");
        material.videoHeight = readInt(json, "video_height", "videoHeight", "height");
        return material;
    }

    /**
     * 判断是否包含源媒体载荷数据
     * <p>
     * 检查是否存在URL或Base64编码内容，用于判断是否需要上传媒体文件。
     * </p>
     *
     * @return true-包含源数据（URL或Base64），false-不包含
     */
    public boolean hasSourcePayload() {
        return StringUtils.hasText(url) || StringUtils.hasText(base64);
    }

    /**
     * 转换为回复媒体项对象
     * <p>
     * 将内部素材模型转换为消息回复所需的媒体项格式，
     * 提取核心字段（URL、Base64、文件名、内容类型、高清标识、语音时长）。
     * </p>
     *
     * @return 回复媒体项对象
     */
    public ReplyMediaItem toReplyMediaItem() {
        ReplyMediaItem item = new ReplyMediaItem();
        item.setUrl(url);
        item.setBase64(base64);
        item.setFilename(StringUtils.hasText(fileName) ? fileName : filename);
        item.setContentType(contentType);
        item.setIsHd(isHd);
        item.setVoiceTime(voiceTime);
        return item;
    }

    /**
     * 从JSON对象中读取字符串值（支持多个候选键名）
     * <p>
     * 按顺序尝试读取指定的多个键名，返回第一个非空值。
     * 用于兼容不同命名风格的JSON数据源（驼峰、下划线等）。
     * </p>
     *
     * @param json JSON对象
     * @param keys 候选键名数组，按优先级顺序排列
     * @return 第一个非空的字符串值，若所有键都不存在或为空则返回null
     */
    public static String readText(JSONObject json, String... keys) {
        for (String key : keys) {
            String value = json.getString(key);
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 从JSON对象中读取整数值（支持多个候选键名）
     * <p>
     * 按顺序尝试读取指定的多个键名，返回第一个非null值。
     * 用于兼容不同命名风格的JSON数据源（驼峰、下划线等）。
     * </p>
     *
     * @param json JSON对象
     * @param keys 候选键名数组，按优先级顺序排列
     * @return 第一个非null的整数值，若所有键都不存在或为null则返回null
     */
    public static Integer readInt(JSONObject json, String... keys) {
        for (String key : keys) {
            Integer value = json.getInteger(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public String getFilename() {
        return filename;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCdnkey() {
        return cdnkey;
    }

    public String getAeskey() {
        return aeskey;
    }

    public String getMd5() {
        return md5;
    }

    public String getFileId() {
        return fileId;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getThumbImageHeight() {
        return thumbImageHeight;
    }

    public Integer getThumbImageWidth() {
        return thumbImageWidth;
    }

    public Integer getThumbFileSize() {
        return thumbFileSize;
    }

    public String getThumbFileMd5() {
        return thumbFileMd5;
    }

    public Integer getIsHd() {
        return isHd;
    }

    public Integer getVoiceTime() {
        return voiceTime;
    }

    public Integer getVideoDuration() {
        return videoDuration;
    }

    public Integer getVideoWidth() {
        return videoWidth;
    }

    public Integer getVideoHeight() {
        return videoHeight;
    }

}
