package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.message.SendImageRequest;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.api.vo.response.message.SendMsgResponse;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomerReplyService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerReplyService.class);

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private CdnImageService cdnImageService;

    @Autowired
    private CdnFileService cdnFileService;

    /**
     * 统一处理下游回调后的自动回复，当前兼容纯文本 reply 和图片集合 images。
     */
    public void sendReplyToCustomer(WxMessageInfo wxMessageInfo, WxUserInfo receiverUser, String callbackBody) {
        if (!StringUtils.hasText(callbackBody)) {
            logger.warn("Callback body is empty, skip reply. msgId={}", wxMessageInfo.getMsgId());
            return;
        }

        JSONObject callbackResult = parseCallbackResult(callbackBody);
        if (callbackResult == null || !callbackResult.getBooleanValue("ok")) {
            logger.warn("Callback result is not ok, skip reply. msgId={}, body={}", wxMessageInfo.getMsgId(), callbackBody);
            return;
        }

        ReplyTarget target = resolveReplyTarget(wxMessageInfo, receiverUser);
        if (target == null) {
            return;
        }

        sendTextReply(target, wxMessageInfo, callbackResult.getString("reply"));
        sendImageReplies(target, wxMessageInfo, callbackResult);
        sendVoiceReplies(target, wxMessageInfo, callbackResult);
    }

    /**
     * 解析业务回调 JSON，解析失败直接跳过回复，避免影响主消息链路。
     */
    private JSONObject parseCallbackResult(String callbackBody) {
        try {
            return JSON.parseObject(callbackBody);
        } catch (Exception e) {
            logger.warn("Failed to parse callback body. body={}", callbackBody, e);
            return null;
        }
    }

    /**
     * 计算回复目标。
     * 单聊回复发送给 sender，群聊回复发送给 roomId。
     */
    private ReplyTarget resolveReplyTarget(WxMessageInfo wxMessageInfo, WxUserInfo receiverUser) {
        boolean isRoomMessage = StringUtils.hasText(wxMessageInfo.getRoomId());
        Long sendUserid = isRoomMessage ? parseLongSafely(wxMessageInfo.getRoomId()) : wxMessageInfo.getSender();
        if (sendUserid == null) {
            logger.warn(
                    "Reply target is invalid. msgId={}, roomId={}, sender={}",
                    wxMessageInfo.getMsgId(),
                    wxMessageInfo.getRoomId(),
                    wxMessageInfo.getSender()
            );
            return null;
        }
        return new ReplyTarget(receiverUser.getUuid(), sendUserid, isRoomMessage, wxMessageInfo.getKfId());
    }

    /**
     * 处理纯文本 reply，保持现有文本自动回复逻辑兼容。
     */
    private void sendTextReply(ReplyTarget target, WxMessageInfo wxMessageInfo, String reply) {
        if (!StringUtils.hasText(reply)) {
            return;
        }

        SendTextRequest request = SendTextRequest.builder()
                .uuid(target.getUuid())
                .send_userid(target.getSendUserid())
                .isRoom(target.getRoomMessage())
                .content(reply)
                .kf_id(target.getKfId())
                .build();
        ApiResult<SendMsgResponse> sendResult = messageSendService.sendText(request);
        logSendResult("text", wxMessageInfo, sendResult);
    }

    /**
     * 处理图片集合回复。
     * 每张图先上传 CDN，再拼装图片消息参数调用发送接口。
     */
    private void sendImageReplies(ReplyTarget target, WxMessageInfo wxMessageInfo, JSONObject callbackResult) {
        JSONArray imageArray = resolveReplyImages(callbackResult);
        if (imageArray == null || imageArray.isEmpty()) {
            return;
        }

        for (int i = 0; i < imageArray.size(); i++) {
            Object item = imageArray.get(i);
            try {
                JSONObject imagePayload = normalizeImagePayload(item);
                CdnUploadResponse uploadResponse = cdnImageService.uploadImage(target.getUuid(), imagePayload);
                SendImageRequest request = buildSendImageRequest(
                        target,
                        imagePayload,
                        validateUploadResponse(uploadResponse)
                );
                ApiResult<SendMsgResponse> sendResult = messageSendService.sendImage(request);
                logSendResult("image", wxMessageInfo, sendResult);
            } catch (Exception e) {
                logger.error(
                        "Failed to send reply image. msgId={}, imageIndex={}, payload={}",
                        wxMessageInfo.getMsgId(),
                        i,
                        item,
                        e
                );
            }
        }
    }

    /**
     * 处理语音回复。
     * 语音文件走文件 CDN 上传链路，文件内容兼容普通文件和 .silk 语音文件。
     */
    private void sendVoiceReplies(ReplyTarget target, WxMessageInfo wxMessageInfo, JSONObject callbackResult) {
        JSONArray voiceArray = resolveReplyVoices(callbackResult);
        if (voiceArray == null || voiceArray.isEmpty()) {
            return;
        }

        for (int i = 0; i < voiceArray.size(); i++) {
            Object item = voiceArray.get(i);
            try {
                JSONObject voicePayload = normalizeVoicePayload(item);
                CdnUploadResponse uploadResponse = cdnFileService.uploadFile(target.getUuid(), voicePayload);
                SendVoiceRequest request = buildSendVoiceRequest(
                        target,
                        voicePayload,
                        validateFileUploadResponse(uploadResponse)
                );
                ApiResult<SendMsgResponse> sendResult = messageSendService.sendVoice(request);
                logSendResult("voice", wxMessageInfo, sendResult);
            } catch (Exception e) {
                logger.error(
                        "Failed to send reply voice. msgId={}, voiceIndex={}, payload={}",
                        wxMessageInfo.getMsgId(),
                        i,
                        item,
                        e
                );
            }
        }
    }

    /**
     * 兼容多种图片数组字段名，并兼容字符串化 JSON 数组。
     */
    private JSONArray resolveReplyImages(JSONObject callbackResult) {
        Object images = firstNonNull(
                callbackResult.get("images"),
                callbackResult.get("replyImages"),
                callbackResult.get("reply_images"),
                callbackResult.get("imageList")
        );
        if (images instanceof JSONArray) {
            return (JSONArray) images;
        }
        if (images instanceof JSONObject) {
            JSONArray array = new JSONArray();
            array.add(images);
            return array;
        }
        if (images instanceof String && StringUtils.hasText((String) images)) {
            String text = ((String) images).trim();
            if (text.startsWith("[")) {
                try {
                    return JSON.parseArray(text);
                } catch (Exception e) {
                    logger.warn("Failed to parse image array string. text={}", text, e);
                    return null;
                }
            }
            JSONArray array = new JSONArray();
            array.add(text);
            return array;
        }
        return null;
    }

    /**
     * 兼容多种语音字段名，并兼容字符串化 JSON 数组。
     */
    private JSONArray resolveReplyVoices(JSONObject callbackResult) {
        Object voices = firstNonNull(
                callbackResult.get("voice"),
                callbackResult.get("voices"),
                callbackResult.get("replyVoice"),
                callbackResult.get("reply_voice"),
                callbackResult.get("audio"),
                callbackResult.get("audios")
        );
        if (voices instanceof JSONArray) {
            return (JSONArray) voices;
        }
        if (voices instanceof JSONObject) {
            JSONArray array = new JSONArray();
            array.add(voices);
            return array;
        }
        if (voices instanceof String && StringUtils.hasText((String) voices)) {
            String text = ((String) voices).trim();
            if (text.startsWith("[")) {
                try {
                    return JSON.parseArray(text);
                } catch (Exception e) {
                    logger.warn("Failed to parse voice array string. text={}", text, e);
                    return null;
                }
            }
            JSONArray array = new JSONArray();
            array.add(text);
            return array;
        }
        return null;
    }

    /**
     * 将单个图片项归一化为 JSONObject，兼容纯字符串 URL、纯字符串 base64 和 JSON 对象三种输入。
     */
    private JSONObject normalizeImagePayload(Object item) {
        if (item instanceof JSONObject) {
            return (JSONObject) item;
        }
        if (item instanceof String) {
            String value = ((String) item).trim();
            if (value.startsWith("{")) {
                return JSON.parseObject(value);
            }
            JSONObject payload = new JSONObject();
            if (value.startsWith("http://") || value.startsWith("https://")) {
                payload.put("url", value);
            } else {
                payload.put("base64", value);
            }
            return payload;
        }
        throw new IllegalArgumentException("Unsupported image payload type: " + item);
    }

    /**
     * 将单个语音项归一化为 JSONObject，兼容纯字符串 URL、纯字符串 base64 和 JSON 对象三种输入。
     */
    private JSONObject normalizeVoicePayload(Object item) {
        JSONObject payload;
        if (item instanceof JSONObject) {
            payload = (JSONObject) item;
        } else if (item instanceof String) {
            String value = ((String) item).trim();
            if (value.startsWith("{")) {
                payload = JSON.parseObject(value);
            } else {
                payload = new JSONObject();
                if (value.startsWith("http://") || value.startsWith("https://")) {
                    payload.put("url", value);
                } else {
                    payload.put("base64", value);
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported voice payload type: " + item);
        }

        if (!StringUtils.hasText(payload.getString("filename")) && !StringUtils.hasText(payload.getString("fileName"))) {
            payload.put("filename", "reply-" + System.currentTimeMillis() + ".silk");
        }
        return payload;
    }

    /**
     * 发送图片消息前校验 CDN 上传结果，缺少关键媒体字段时直接失败。
     */
    private CdnUploadResponse validateUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null) {
            throw new IllegalStateException("CDN upload response is empty");
        }
        if (!StringUtils.hasText(uploadResponse.getCdn_key())) {
            throw new IllegalStateException("CDN upload response is missing cdn_key");
        }
        if (!StringUtils.hasText(uploadResponse.getAes_key())) {
            throw new IllegalStateException("CDN upload response is missing aes_key");
        }
        if (!StringUtils.hasText(uploadResponse.getMd5())) {
            throw new IllegalStateException("CDN upload response is missing md5");
        }
        if (uploadResponse.getSize() == null) {
            throw new IllegalStateException("CDN upload response is missing size");
        }
        return uploadResponse;
    }

    /**
     * 发送语音消息前校验文件 CDN 上传结果。
     */
    private CdnUploadResponse validateFileUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null) {
            throw new IllegalStateException("CDN file upload response is empty");
        }
        if (!StringUtils.hasText(resolveCdnKey(uploadResponse))) {
            throw new IllegalStateException("CDN file upload response is missing cdn key");
        }
        if (!StringUtils.hasText(uploadResponse.getAes_key())) {
            throw new IllegalStateException("CDN file upload response is missing aes_key");
        }
        if (!StringUtils.hasText(uploadResponse.getMd5())) {
            throw new IllegalStateException("CDN file upload response is missing md5");
        }
        if (uploadResponse.getSize() == null) {
            throw new IllegalStateException("CDN file upload response is missing size");
        }
        return uploadResponse;
    }

    /**
     * 将 CDN 上传结果映射成 SendCDNImgMsg 所需的图片消息请求体。
     */
    private SendImageRequest buildSendImageRequest(ReplyTarget target,
                                                   JSONObject imagePayload,
                                                   CdnUploadResponse uploadResponse) {
        return SendImageRequest.builder()
                .uuid(target.getUuid())
                .send_userid(target.getSendUserid())
                .kf_id(target.getKfId())
                .isRoom(target.getRoomMessage())
                .cdnkey(uploadResponse.getCdn_key())
                .aeskey(uploadResponse.getAes_key())
                .md5(uploadResponse.getMd5())
                .fileSize(uploadResponse.getSize())
                .width(uploadResponse.getWidth())
                .height(uploadResponse.getHeight())
                .thumb_image_height(uploadResponse.getThumb_image_height())
                .thumb_image_width(uploadResponse.getThumb_image_width())
                .thumb_file_size(uploadResponse.getThumb_file_size())
                .thumb_file_md5(uploadResponse.getThumb_file_md5())
                .is_hd(resolveIsHd(imagePayload))
                .build();
    }

    /**
     * 将 CDN 文件上传结果映射成 SendCDNVoiceMsg 所需的语音消息请求体。
     */
    private SendVoiceRequest buildSendVoiceRequest(ReplyTarget target,
                                                   JSONObject voicePayload,
                                                   CdnUploadResponse uploadResponse) {
        return SendVoiceRequest.builder()
                .uuid(target.getUuid())
                .send_userid(target.getSendUserid())
                .kf_id(target.getKfId())
                .isRoom(target.getRoomMessage())
                .cdnkey(resolveCdnKey(uploadResponse))
                .aeskey(uploadResponse.getAes_key())
                .md5(uploadResponse.getMd5())
                .voice_time(resolveVoiceDuration(voicePayload))
                .fileSize(uploadResponse.getSize())
                .build();
    }

    /**
     * 文件类 CDN 上传有时返回 cdn_key，有时只返回 fileid，这里统一兜底。
     */
    private String resolveCdnKey(CdnUploadResponse uploadResponse) {
        if (StringUtils.hasText(uploadResponse.getCdn_key())) {
            return uploadResponse.getCdn_key();
        }
        return uploadResponse.getFileid();
    }

    /**
     * 兼容 is_hd / isHd 两种字段名。
     */
    private Integer resolveIsHd(JSONObject imagePayload) {
        Integer isHd = imagePayload.getInteger("is_hd");
        if (isHd != null) {
            return isHd;
        }
        return imagePayload.getInteger("isHd");
    }

    /**
     * 兼容 voice_time / voiceTime / duration 三种字段名。
     */
    private Integer resolveVoiceDuration(JSONObject voicePayload) {
        Integer voiceTime = voicePayload.getInteger("voice_time");
        if (voiceTime != null) {
            return voiceTime;
        }
        voiceTime = voicePayload.getInteger("voiceTime");
        if (voiceTime != null) {
            return voiceTime;
        }
        voiceTime = voicePayload.getInteger("duration");
        if (voiceTime != null) {
            return voiceTime;
        }
        throw new IllegalStateException("Reply voice is missing voice_time");
    }

    /**
     * 统一记录自动回复发送结果，便于排查文本和图片两类回复。
     */
    private void logSendResult(String replyType, WxMessageInfo wxMessageInfo, ApiResult<SendMsgResponse> sendResult) {
        logger.info(
                "Reply send finished. type={}, msgId={}, receiver={}, sender={}, code={}, msg={}",
                replyType,
                wxMessageInfo.getMsgId(),
                wxMessageInfo.getReceiver(),
                wxMessageInfo.getSender(),
                sendResult == null ? null : sendResult.getCode(),
                sendResult == null ? null : sendResult.getMsg()
        );
    }

    /**
     * 群聊 roomId 在库里是字符串，这里安全转换为发送接口需要的 Long。
     */
    private Long parseLongSafely(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse roomId: {}", value);
            return null;
        }
    }

    /**
     * 从多个候选对象里取第一个非 null 值，兼容不同回调协议字段名。
     */
    private Object firstNonNull(Object... values) {
        if (values == null) {
            return null;
        }
        for (Object value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static class ReplyTarget {
        private final String uuid;
        private final Long sendUserid;
        private final Boolean roomMessage;
        private final Long kfId;

        private ReplyTarget(String uuid, Long sendUserid, Boolean roomMessage, Long kfId) {
            this.uuid = uuid;
            this.sendUserid = sendUserid;
            this.roomMessage = roomMessage;
            this.kfId = kfId;
        }

        public String getUuid() {
            return uuid;
        }

        public Long getSendUserid() {
            return sendUserid;
        }

        public Boolean getRoomMessage() {
            return roomMessage;
        }

        public Long getKfId() {
            return kfId;
        }
    }
}
