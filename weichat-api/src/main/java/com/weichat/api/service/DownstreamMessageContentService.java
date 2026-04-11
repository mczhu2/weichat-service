package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.vo.request.cdn.DownloadFileRequest;
import com.weichat.api.vo.request.cdn.DownloadWeChatFileRequest;
import com.weichat.common.entity.WxMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
     * 统一解析发送给下游业务系统的 content 字段。
     * 这里做协议收口，MessageStrategy 只负责编排，不直接关心媒体下载细节。
     */
    public String resolveCallbackContent(WxMessageInfo wxMessageInfo, String uuid) {
        String plainContent = resolvePlainContent(wxMessageInfo);
        if (StringUtils.hasText(plainContent)) {
            return plainContent;
        }
        if (wxMessageInfo == null || !StringUtils.hasText(uuid)) {
            return plainContent;
        }

        if (isImageMessage(wxMessageInfo)) {
            return resolveImageContent(wxMessageInfo, uuid);
        }
        if (isVideoMessage(wxMessageInfo)) {
            return resolveVideoContent(wxMessageInfo, uuid);
        }
        if (isVoiceMessage(wxMessageInfo)) {
            return resolveVoiceContent(wxMessageInfo, uuid);
        }
        return buildFallbackContent(wxMessageInfo, "message");
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
     * 图片消息解析：
     * 优先把微信资源地址转成外部可访问 URL，再封装成给 AI 识别的 content 文本。
     */
    private String resolveImageContent(WxMessageInfo wxMessageInfo, String uuid) {
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
        return wrapMediaContent("image", mediaUrl, buildFallbackContent(wxMessageInfo, "image"));
    }

    /**
     * 视频消息解析：
     * 使用外部联系人文件下载接口拿到临时 URL，失败时只回退占位内容。
     */
    private String resolveVideoContent(WxMessageInfo wxMessageInfo, String uuid) {
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
        return wrapMediaContent("video", mediaUrl, buildFallbackContent(wxMessageInfo, "video"));
    }

    /**
     * 语音消息解析：
     * 语音没有直接 file_url，这里走 DownloadFile(filetype=5) 拿下载地址。
     * 即使下载失败，也不会抛异常影响主流程。
     */
    private String resolveVoiceContent(WxMessageInfo wxMessageInfo, String uuid) {
        String mediaUrl = downloadVoiceMedia(
                uuid,
                wxMessageInfo.getVoiceId(),
                wxMessageInfo.getAesKey(),
                toInteger(wxMessageInfo.getVoiceSize()),
                buildVoiceFileName(wxMessageInfo),
                wxMessageInfo.getMsgId()
        );
        return wrapMediaContent("voice", mediaUrl, buildFallbackContent(wxMessageInfo, "voice"));
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
     * 把媒资 URL 包成统一 content 文本。
     * 约定格式为 [image]/[video]/[voice] + url，方便下游 AI 直接解析。
     */
    private String wrapMediaContent(String mediaType, String mediaUrl, String fallbackContent) {
        if (StringUtils.hasText(mediaUrl)) {
            return "[" + mediaType + "] " + mediaUrl;
        }
        return fallbackContent;
    }

    /**
     * 下载失败时的降级内容。
     * 优先带上 voiceId/fileId/messageId，保证下游至少还能拿到原始媒资标识。
     */
    private String buildFallbackContent(WxMessageInfo wxMessageInfo, String mediaType) {
        if (wxMessageInfo == null) {
            return "[" + mediaType + "]";
        }
        if (StringUtils.hasText(wxMessageInfo.getVoiceId())) {
            return "[" + mediaType + "] " + wxMessageInfo.getVoiceId();
        }
        if (StringUtils.hasText(wxMessageInfo.getFileId())) {
            return "[" + mediaType + "] " + wxMessageInfo.getFileId();
        }
        if (StringUtils.hasText(wxMessageInfo.getMessageId())) {
            return "[" + mediaType + "] " + wxMessageInfo.getMessageId();
        }
        return "[" + mediaType + "]";
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
