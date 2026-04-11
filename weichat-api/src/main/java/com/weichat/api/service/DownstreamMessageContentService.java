package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.vo.callback.DownstreamCallbackPayload;
import com.weichat.api.vo.callback.DownstreamMediaVo;
import com.weichat.api.vo.request.cdn.DownloadFileRequest;
import com.weichat.api.vo.request.cdn.DownloadWeChatFileRequest;
import com.weichat.common.entity.WxMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * 下游业务回调消息内容解析服务。
 * 文本消息直接透传，图片/视频/语音消息会先尝试解析成可访问的资源 URL，
 * 解析失败时只降级返回占位内容，不影响主消息回调流程。
 */
@Service
public class DownstreamMessageContentService {

    private static final Logger logger = LoggerFactory.getLogger(DownstreamMessageContentService.class);

    private static final int MSGTYPE_IMAGE = 101;
    private static final int MSGTYPE_VOICE = 16;
    private static final int MSGTYPE_VIDEO = 103;
    private static final int DOWNLOAD_FILETYPE_VOICE = 5;

    @Autowired
    private WxWorkApiClient client;

    /**
     * 统一解析发给下游业务系统的消息载荷。
     * 文本放 content，媒体放 medias，避免把所有消息都塞进 content 破坏字段语义。
     */
    public DownstreamCallbackPayload resolveCallbackPayload(WxMessageInfo wxMessageInfo, String uuid) {
        DownstreamCallbackPayload payload = new DownstreamCallbackPayload();
        payload.setContent(resolvePlainContent(wxMessageInfo));

        if (wxMessageInfo == null || !StringUtils.hasText(uuid)) {
            return payload;
        }
        if (isImageMessage(wxMessageInfo)) {
            payload.setMedias(Collections.singletonList(resolveImageMedia(wxMessageInfo, uuid)));
            return payload;
        }
        if (isVideoMessage(wxMessageInfo)) {
            payload.setMedias(Collections.singletonList(resolveVideoMedia(wxMessageInfo, uuid)));
            return payload;
        }
        if (isVoiceMessage(wxMessageInfo)) {
            payload.setMedias(Collections.singletonList(resolveVoiceMedia(wxMessageInfo, uuid)));
        }
        return payload;
    }

    /**
     * 优先处理文本类消息，保持原有纯文本、链接、小程序标题等内容透传逻辑。
     */
    private String resolvePlainContent(WxMessageInfo wxMessageInfo) {
        if (wxMessageInfo == null) {
            return "";
        }
        if (StringUtils.hasText(wxMessageInfo.getContent())) {
            return wxMessageInfo.getContent();
        }
        if (StringUtils.hasText(wxMessageInfo.getTitle())) {
            return wxMessageInfo.getTitle();
        }
        if (StringUtils.hasText(wxMessageInfo.getDesc())) {
            return wxMessageInfo.getDesc();
        }
        if (StringUtils.hasText(wxMessageInfo.getUrl())) {
            return wxMessageInfo.getUrl();
        }
        return "";
    }

    /**
     * 解析图片消息的结构化媒体信息。
     */
    private DownstreamMediaVo resolveImageMedia(WxMessageInfo wxMessageInfo, String uuid) {
        String mediaUrl = downloadWeChatMedia(
                uuid,
                firstNonBlank(wxMessageInfo.getOpenimCdnLdurl(), wxMessageInfo.getFileId()),
                wxMessageInfo.getOpenimCdnAuthkey(),
                wxMessageInfo.getAesKey(),
                firstNonNullInteger(wxMessageInfo.getOpenimCdnLdSize(), toInteger(wxMessageInfo.getFileSize())),
                buildImageFileName(wxMessageInfo),
                "image",
                wxMessageInfo.getMsgId()
        );
        DownstreamMediaVo media = buildBaseMedia(wxMessageInfo, "image", mediaUrl);
        media.setWidth(wxMessageInfo.getWidth());
        media.setHeight(wxMessageInfo.getHeight());
        return media;
    }

    /**
     * 解析视频消息的结构化媒体信息。
     */
    private DownstreamMediaVo resolveVideoMedia(WxMessageInfo wxMessageInfo, String uuid) {
        String mediaUrl = downloadWeChatMedia(
                uuid,
                wxMessageInfo.getFileId(),
                wxMessageInfo.getOpenimCdnAuthkey(),
                wxMessageInfo.getAesKey(),
                toInteger(wxMessageInfo.getFileSize()),
                buildVideoFileName(wxMessageInfo),
                "video",
                wxMessageInfo.getMsgId()
        );
        DownstreamMediaVo media = buildBaseMedia(wxMessageInfo, "video", mediaUrl);
        media.setDuration(wxMessageInfo.getVideoDuration());
        media.setWidth(firstNonNullInteger(wxMessageInfo.getVideoWidth(), wxMessageInfo.getWidth()));
        media.setHeight(firstNonNullInteger(wxMessageInfo.getVideoHeight(), wxMessageInfo.getHeight()));
        media.setPreviewUrl(wxMessageInfo.getPreviewImgUrl());
        return media;
    }

    /**
     * 解析语音消息的结构化媒体信息。
     */
    private DownstreamMediaVo resolveVoiceMedia(WxMessageInfo wxMessageInfo, String uuid) {
        String mediaUrl = downloadVoiceMedia(
                uuid,
                wxMessageInfo.getVoiceId(),
                wxMessageInfo.getAesKey(),
                toInteger(wxMessageInfo.getVoiceSize()),
                buildVoiceFileName(wxMessageInfo),
                wxMessageInfo.getMsgId()
        );
        DownstreamMediaVo media = buildBaseMedia(wxMessageInfo, "voice", mediaUrl);
        media.setDuration(wxMessageInfo.getVoiceTime());
        return media;
    }

    /**
     * 下载图片/视频的微信资源。
     * 这类消息回调里通常会带 tpdownloadmedia 的原始 url，配合 auth_key/aes_key/size 获取可访问地址。
     */
    private String downloadWeChatMedia(String uuid,
                                       String sourceUrl,
                                       String authKey,
                                       String aesKey,
                                       Integer size,
                                       String fileName,
                                       String mediaType,
                                       Long msgId) {
        if (!StringUtils.hasText(sourceUrl)
                || !StringUtils.hasText(authKey)
                || !StringUtils.hasText(aesKey)
                || size == null
                || size <= 0) {
            logger.warn(
                    "Skip {} media download because params are incomplete. msgId={}, url={}, authKeyPresent={}, aesKeyPresent={}, size={}",
                    mediaType,
                    msgId,
                    sourceUrl,
                    StringUtils.hasText(authKey),
                    StringUtils.hasText(aesKey),
                    size
            );
            return null;
        }

        try {
            DownloadWeChatFileRequest request = DownloadWeChatFileRequest.builder()
                    .uuid(uuid)
                    .fileUrl(sourceUrl)
                    .authKey(authKey)
                    .aesKey(aesKey)
                    .fileName(fileName)
                    .size(size)
                    .build();
            JSONObject response = client.post("/wxwork/DownloadWeChatFile", (JSONObject) JSON.toJSON(request));
            return extractDownloadUrl(response, mediaType, msgId);
        } catch (Exception e) {
            logger.warn("Failed to download {} media url. msgId={}", mediaType, msgId, e);
            return null;
        }
    }

    /**
     * 下载语音资源。
     * 语音消息使用 voice_id + aes_key + filetype=5 的协议，不走 DownloadWeChatFile。
     */
    private String downloadVoiceMedia(String uuid,
                                      String fileId,
                                      String aesKey,
                                      Integer size,
                                      String fileName,
                                      Long msgId) {
        if (!StringUtils.hasText(fileId) || !StringUtils.hasText(aesKey) || size == null || size <= 0) {
            logger.warn(
                    "Skip voice media download because params are incomplete. msgId={}, fileIdPresent={}, aesKeyPresent={}, size={}",
                    msgId,
                    StringUtils.hasText(fileId),
                    StringUtils.hasText(aesKey),
                    size
            );
            return null;
        }

        try {
            DownloadFileRequest request = DownloadFileRequest.builder()
                    .uuid(uuid)
                    .fileId(fileId)
                    .aesKey(aesKey)
                    .fileType(DOWNLOAD_FILETYPE_VOICE)
                    .fileName(fileName)
                    .size(size)
                    .build();
            JSONObject response = client.post("/wxwork/DownloadFile", (JSONObject) JSON.toJSON(request));
            return extractDownloadUrl(response, "voice", msgId);
        } catch (Exception e) {
            logger.warn("Failed to download voice media url. msgId={}", msgId, e);
            return null;
        }
    }

    /**
     * 从下载接口原始响应里提取最终 URL。
     * 这里兼容 errcode/errorcode 两种错误码字段，也兼容 data 为字符串或对象两种结构。
     */
    private String extractDownloadUrl(JSONObject response, String mediaType, Long msgId) {
        if (response == null) {
            logger.warn("Download {} url returned null result. msgId={}", mediaType, msgId);
            return null;
        }
        int code = response.containsKey("errcode") ? response.getIntValue("errcode") : response.getIntValue("errorcode");
        if (code != 0) {
            logger.warn(
                    "Download {} url failed. msgId={}, code={}, msg={}",
                    mediaType,
                    msgId,
                    code,
                    firstNonBlank(response.getString("errmsg"), response.getString("msg"))
            );
            return null;
        }
        Object data = response.get("data");
        if (data instanceof String && StringUtils.hasText((String) data)) {
            return (String) data;
        }
        if (data instanceof JSONObject) {
            String url = firstNonBlank(((JSONObject) data).getString("url"), ((JSONObject) data).getString("downloadUrl"));
            if (StringUtils.hasText(url)) {
                return url;
            }
        }
        if (!StringUtils.hasText(response.getString("data"))) {
            logger.warn("Download {} url returned empty data. msgId={}", mediaType, msgId);
            return null;
        }
        return response.getString("data");
    }

    /**
     * 下载失败时的降级内容。
     * 优先带上 voiceId/fileId/messageId，保证下游至少还能拿到原始媒资标识。
     */
    private String buildRawMediaId(WxMessageInfo wxMessageInfo) {
        if (wxMessageInfo == null) {
            return null;
        }
        if (StringUtils.hasText(wxMessageInfo.getVoiceId())) {
            return wxMessageInfo.getVoiceId();
        }
        if (StringUtils.hasText(wxMessageInfo.getFileId())) {
            return wxMessageInfo.getFileId();
        }
        return wxMessageInfo.getMessageId();
    }

    /**
     * 构建媒体公共字段。
     * mediaUrl 获取失败时保留 rawMediaId，不影响主流程，交给下游自行兜底。
     */
    private DownstreamMediaVo buildBaseMedia(WxMessageInfo wxMessageInfo, String mediaType, String mediaUrl) {
        DownstreamMediaVo media = new DownstreamMediaVo();
        media.setMediaType(mediaType);
        media.setMediaUrl(mediaUrl);
        media.setRawMediaId(buildRawMediaId(wxMessageInfo));
        media.setFileName(resolveMediaFileName(mediaType, wxMessageInfo));
        media.setSize(resolveMediaSize(wxMessageInfo));
        return media;
    }

    /**
     * 统一解析下游展示的媒体文件名。
     */
    private String resolveMediaFileName(String mediaType, WxMessageInfo wxMessageInfo) {
        if ("image".equals(mediaType)) {
            return buildImageFileName(wxMessageInfo);
        }
        if ("video".equals(mediaType)) {
            return buildVideoFileName(wxMessageInfo);
        }
        if ("voice".equals(mediaType)) {
            return buildVoiceFileName(wxMessageInfo);
        }
        return buildFileName("media", wxMessageInfo, "bin");
    }

    /**
     * 统一解析媒体大小字段。
     */
    private Integer resolveMediaSize(WxMessageInfo wxMessageInfo) {
        if (wxMessageInfo == null) {
            return null;
        }
        return firstNonNullInteger(
                toInteger(wxMessageInfo.getVoiceSize()),
                wxMessageInfo.getOpenimCdnLdSize(),
                toInteger(wxMessageInfo.getFileSize()),
                wxMessageInfo.getSize()
        );
    }

    /**
     * 生成图片下载时使用的文件名。
     */
    private String buildImageFileName(WxMessageInfo wxMessageInfo) {
        return buildFileName("image", wxMessageInfo, "jpg");
    }

    /**
     * 生成视频下载时使用的文件名。
     */
    private String buildVideoFileName(WxMessageInfo wxMessageInfo) {
        return buildFileName("video", wxMessageInfo, "mp4");
    }

    /**
     * 生成语音下载时使用的文件名。
     */
    private String buildVoiceFileName(WxMessageInfo wxMessageInfo) {
        return buildFileName("voice", wxMessageInfo, "silk");
    }

    /**
     * 优先使用消息回调里的 file_name，没有的话再按 msgId 生成兜底文件名。
     */
    private String buildFileName(String prefix, WxMessageInfo wxMessageInfo, String defaultExtension) {
        if (wxMessageInfo != null && StringUtils.hasText(wxMessageInfo.getFileName())) {
            return wxMessageInfo.getFileName();
        }
        String suffix = wxMessageInfo != null && wxMessageInfo.getMsgId() != null
                ? String.valueOf(wxMessageInfo.getMsgId())
                : String.valueOf(System.currentTimeMillis());
        return prefix + "-" + suffix + "." + defaultExtension;
    }

    /**
     * 判断是否图片消息。
     */
    private boolean isImageMessage(WxMessageInfo wxMessageInfo) {
        return wxMessageInfo.getMsgtype() != null && wxMessageInfo.getMsgtype() == MSGTYPE_IMAGE;
    }

    /**
     * 判断是否视频消息。
     */
    private boolean isVideoMessage(WxMessageInfo wxMessageInfo) {
        return wxMessageInfo.getMsgtype() != null && wxMessageInfo.getMsgtype() == MSGTYPE_VIDEO;
    }

    /**
     * 判断是否语音消息。
     */
    private boolean isVoiceMessage(WxMessageInfo wxMessageInfo) {
        return wxMessageInfo.getMsgtype() != null && wxMessageInfo.getMsgtype() == MSGTYPE_VOICE;
    }

    /**
     * 从多个候选字符串里取第一个非空值，兼容不同消息字段别名。
     */
    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 从多个候选整型里取第一个非 null 值。
     */
    private Integer firstNonNullInteger(Integer... values) {
        if (values == null) {
            return null;
        }
        for (Integer value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 安全转换消息回调里的 Long 大小字段，避免和下载接口的 Integer 入参类型不一致。
     */
    private Integer toInteger(Long value) {
        if (value == null) {
            return null;
        }
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return value.intValue();
    }
}
