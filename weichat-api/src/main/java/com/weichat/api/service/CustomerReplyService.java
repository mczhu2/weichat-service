package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.callback.CustomerReplyCallbackResult;
import com.weichat.api.vo.callback.ReplyMediaItem;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * Handle downstream callback replies. Currently supports text, image and voice replies.
     */
    public void sendReplyToCustomer(WxMessageInfo wxMessageInfo, WxUserInfo receiverUser, String callbackBody) {
        if (!StringUtils.hasText(callbackBody)) {
            logger.warn("Callback body is empty, skip reply. msgId={}", wxMessageInfo.getMsgId());
            return;
        }

        CustomerReplyCallbackResult callbackResult = parseCallbackResult(callbackBody);
        if (callbackResult == null || !callbackResult.isSuccess()) {
            logger.warn("Callback result is not ok, skip reply. msgId={}, body={}", wxMessageInfo.getMsgId(), callbackBody);
            return;
        }

        ReplyTarget target = resolveReplyTarget(wxMessageInfo, receiverUser);
        if (target == null) {
            return;
        }

        sendTextReply(target, wxMessageInfo, callbackResult.getReply());
        sendImageReplies(target, wxMessageInfo, callbackResult.getImages());
        sendVoiceReplies(target, wxMessageInfo, callbackResult.getVoices());
    }

    /**
     * Parse callback JSON into a typed result to avoid passing raw JSONObject in business flow.
     */
    private CustomerReplyCallbackResult parseCallbackResult(String callbackBody) {
        try {
            JSONObject callbackJson = JSON.parseObject(callbackBody);
            if (callbackJson == null) {
                return null;
            }

            CustomerReplyCallbackResult callbackResult = new CustomerReplyCallbackResult();
            callbackResult.setOk(callbackJson.getBoolean("ok"));
            callbackResult.setReply(callbackJson.getString("reply"));
            callbackResult.setImages(resolveReplyImages(callbackJson));
            callbackResult.setVoices(resolveReplyVoices(callbackJson));
            return callbackResult;
        } catch (Exception e) {
            logger.warn("Failed to parse callback body. body={}", callbackBody, e);
            return null;
        }
    }

    /**
     * Resolve the reply target. Direct chat replies sender, room chat replies roomId.
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
     * Send plain text reply content.
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
     * Upload and send all callback images.
     */
    private void sendImageReplies(ReplyTarget target, WxMessageInfo wxMessageInfo, List<ReplyMediaItem> imageItems) {
        if (imageItems == null || imageItems.isEmpty()) {
            return;
        }

        for (int i = 0; i < imageItems.size(); i++) {
            ReplyMediaItem imagePayload = imageItems.get(i);
            try {
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
                        imagePayload,
                        e
                );
            }
        }
    }

    /**
     * Upload and send all callback voices.
     */
    private void sendVoiceReplies(ReplyTarget target, WxMessageInfo wxMessageInfo, List<ReplyMediaItem> voiceItems) {
        if (voiceItems == null || voiceItems.isEmpty()) {
            return;
        }

        for (int i = 0; i < voiceItems.size(); i++) {
            ReplyMediaItem voicePayload = voiceItems.get(i);
            try {
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
                        voicePayload,
                        e
                );
            }
        }
    }

    /**
     * Read image aliases from callback body and normalize them into typed items.
     */
    private List<ReplyMediaItem> resolveReplyImages(JSONObject callbackResult) {
        Object images = firstNonNull(
                callbackResult.get("images"),
                callbackResult.get("replyImages"),
                callbackResult.get("reply_images"),
                callbackResult.get("imageList")
        );
        return resolveReplyMediaItems(images, "image");
    }

    /**
     * Read voice aliases from callback body and normalize them into typed items.
     */
    private List<ReplyMediaItem> resolveReplyVoices(JSONObject callbackResult) {
        Object voices = firstNonNull(
                callbackResult.get("voice"),
                callbackResult.get("voices"),
                callbackResult.get("replyVoice"),
                callbackResult.get("reply_voice"),
                callbackResult.get("audio"),
                callbackResult.get("audios")
        );
        return resolveReplyMediaItems(voices, "voice");
    }

    /**
     * Normalize callback media payloads so the rest of the flow no longer depends on JSONObject.
     */
    private List<ReplyMediaItem> resolveReplyMediaItems(Object mediaPayload, String mediaType) {
        if (mediaPayload == null) {
            return Collections.emptyList();
        }
        if (mediaPayload instanceof JSONArray) {
            return normalizeMediaArray((JSONArray) mediaPayload, mediaType);
        }
        if (mediaPayload instanceof String && StringUtils.hasText((String) mediaPayload)) {
            String text = ((String) mediaPayload).trim();
            if (text.startsWith("[")) {
                try {
                    return normalizeMediaArray(JSON.parseArray(text), mediaType);
                } catch (Exception e) {
                    logger.warn("Failed to parse {} array string. text={}", mediaType, text, e);
                    return Collections.emptyList();
                }
            }
        }

        ReplyMediaItem mediaItem = normalizeMediaPayload(mediaPayload, mediaType);
        if (mediaItem == null) {
            return Collections.emptyList();
        }
        List<ReplyMediaItem> mediaItems = new ArrayList<>(1);
        mediaItems.add(mediaItem);
        return mediaItems;
    }

    /**
     * Normalize each media item independently so one bad item does not block the rest.
     */
    private List<ReplyMediaItem> normalizeMediaArray(JSONArray mediaArray, String mediaType) {
        if (mediaArray == null || mediaArray.isEmpty()) {
            return Collections.emptyList();
        }

        List<ReplyMediaItem> mediaItems = new ArrayList<>(mediaArray.size());
        for (int i = 0; i < mediaArray.size(); i++) {
            Object item = mediaArray.get(i);
            try {
                ReplyMediaItem mediaItem = normalizeMediaPayload(item, mediaType);
                if (mediaItem != null) {
                    mediaItems.add(mediaItem);
                }
            } catch (Exception e) {
                logger.warn("Failed to normalize {} payload. index={}, payload={}", mediaType, i, item, e);
            }
        }
        return mediaItems;
    }

    /**
     * Normalize a single media item. Supported inputs are URL string, base64 string and JSON object.
     */
    private ReplyMediaItem normalizeMediaPayload(Object item, String mediaType) {
        if (item == null) {
            return null;
        }

        if (item instanceof JSONObject) {
            return buildMediaItemFromJson((JSONObject) item, mediaType);
        }
        if (item instanceof String) {
            String value = ((String) item).trim();
            if (!StringUtils.hasText(value)) {
                return null;
            }
            if (value.startsWith("{")) {
                return buildMediaItemFromJson(JSON.parseObject(value), mediaType);
            }

            ReplyMediaItem mediaItem = new ReplyMediaItem();
            if (value.startsWith("http://") || value.startsWith("https://")) {
                mediaItem.setUrl(value);
            } else {
                mediaItem.setBase64(value);
            }
            enrichMediaItem(mediaItem, mediaType);
            return mediaItem;
        }

        throw new IllegalArgumentException("Unsupported " + mediaType + " payload type: " + item);
    }

    /**
     * Map all currently used callback aliases into a single media VO.
     */
    private ReplyMediaItem buildMediaItemFromJson(JSONObject payload, String mediaType) {
        ReplyMediaItem mediaItem = new ReplyMediaItem();
        mediaItem.setUrl(firstNonBlank(
                payload.getString("url"),
                payload.getString("imageUrl"),
                payload.getString("image_url"),
                payload.getString("fileUrl"),
                payload.getString("voiceUrl"),
                payload.getString("audioUrl")
        ));
        mediaItem.setBase64(firstNonBlank(
                payload.getString("base64"),
                payload.getString("data"),
                payload.getString("content")
        ));
        mediaItem.setFilename(firstNonBlank(payload.getString("filename"), payload.getString("fileName")));
        mediaItem.setContentType(firstNonBlank(payload.getString("contentType"), payload.getString("mimeType")));
        mediaItem.setIsHd(firstNonNullInteger(payload.getInteger("is_hd"), payload.getInteger("isHd")));
        mediaItem.setVoiceTime(firstNonNullInteger(
                payload.getInteger("voice_time"),
                payload.getInteger("voiceTime"),
                payload.getInteger("duration")
        ));
        enrichMediaItem(mediaItem, mediaType);
        return mediaItem;
    }

    /**
     * Fill type specific defaults. Voice reply currently defaults to a silk filename.
     */
    private void enrichMediaItem(ReplyMediaItem mediaItem, String mediaType) {
        if ("voice".equals(mediaType)) {
            mediaItem.ensureFilename("reply-" + System.currentTimeMillis() + ".silk");
        }
    }

    /**
     * Validate the image CDN upload response before sending the image message.
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
     * Validate the file CDN upload response before sending the voice message.
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
     * Map image upload response into SendCDNImgMsg request fields.
     */
    private SendImageRequest buildSendImageRequest(ReplyTarget target,
                                                   ReplyMediaItem imagePayload,
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
     * Map file upload response into SendCDNVoiceMsg request fields.
     */
    private SendVoiceRequest buildSendVoiceRequest(ReplyTarget target,
                                                   ReplyMediaItem voicePayload,
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
     * File upload may return cdn_key or only fileid. Use the available one.
     */
    private String resolveCdnKey(CdnUploadResponse uploadResponse) {
        if (StringUtils.hasText(uploadResponse.getCdn_key())) {
            return uploadResponse.getCdn_key();
        }
        return uploadResponse.getFileid();
    }

    /**
     * Resolve optional image HD flag.
     */
    private Integer resolveIsHd(ReplyMediaItem imagePayload) {
        return imagePayload.getIsHd();
    }

    /**
     * Resolve required voice duration.
     */
    private Integer resolveVoiceDuration(ReplyMediaItem voicePayload) {
        Integer voiceTime = voicePayload.getVoiceTime();
        if (voiceTime != null) {
            return voiceTime;
        }
        throw new IllegalStateException("Reply voice is missing voice_time");
    }

    /**
     * Log reply sending result for troubleshooting.
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
     * Safely convert roomId into Long for send APIs.
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
     * Return the first non-null value from alias candidates.
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

    /**
     * Return the first non-blank string from alias candidates.
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
     * Return the first non-null integer from alias candidates.
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
