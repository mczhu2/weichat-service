package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.request.mass.MassTaskMediaPayload;
import org.springframework.util.StringUtils;

public class MassTaskMediaMaterial {

    private String url;
    private String base64;
    private String filename;
    private String fileName;
    private String contentType;
    private String cdnkey;
    private String aeskey;
    private String md5;
    private String fileId;
    private Integer fileSize;
    private Integer width;
    private Integer height;
    private Integer thumbImageHeight;
    private Integer thumbImageWidth;
    private Integer thumbFileSize;
    private String thumbFileMd5;
    private Integer isHd;
    private Integer voiceTime;
    private Integer videoDuration;
    private Integer videoWidth;
    private Integer videoHeight;

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

    public boolean hasSourcePayload() {
        return StringUtils.hasText(url) || StringUtils.hasText(base64);
    }

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

    public static String readText(JSONObject json, String... keys) {
        for (String key : keys) {
            String value = json.getString(key);
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    public static Integer readInt(JSONObject json, String... keys) {
        for (String key : keys) {
            Integer value = json.getInteger(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public String getFilename() { return filename; }
    public String getFileName() { return fileName; }
    public String getCdnkey() { return cdnkey; }
    public String getAeskey() { return aeskey; }
    public String getMd5() { return md5; }
    public String getFileId() { return fileId; }
    public Integer getFileSize() { return fileSize; }
    public Integer getWidth() { return width; }
    public Integer getHeight() { return height; }
    public Integer getThumbImageHeight() { return thumbImageHeight; }
    public Integer getThumbImageWidth() { return thumbImageWidth; }
    public Integer getThumbFileSize() { return thumbFileSize; }
    public String getThumbFileMd5() { return thumbFileMd5; }
    public Integer getIsHd() { return isHd; }
    public Integer getVoiceTime() { return voiceTime; }
    public Integer getVideoDuration() { return videoDuration; }
    public Integer getVideoWidth() { return videoWidth; }
    public Integer getVideoHeight() { return videoHeight; }
}
