package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.callback.CustomerReplyCallbackResult;
import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.request.message.SendFriendReplyRequest;
import com.weichat.api.vo.request.message.SendImageRequest;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.request.message.WecomAgentReplyCallbackRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.api.vo.response.message.SendMsgResponse;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class CustomerReplyService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerReplyService.class);

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private CdnImageService cdnImageService;

    @Autowired
    private CdnFileService cdnFileService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    public ApiResult<Void> handleWecomAgentReplyCallback(WecomAgentReplyCallbackRequest request) {
        if (request == null) {
            return ApiResult.fail("request body is invalid");
        }
        if (!Boolean.TRUE.equals(request.getOk())) {
            logger.warn("Wecom agent reply callback is not ok, skip reply. request={}", JSON.toJSONString(request));
            return ApiResult.success(null);
        }

        ReplyTarget target = resolveReplyTargetFromCallback(request);
        if (target == null) {
            return ApiResult.fail("reply target is invalid");
        }

        ReplyLogContext logContext = ReplyLogContext.fromMessage(
                null,
                resolveCallbackReplySender(request),
                resolveCallbackReplyReceiver(request)
        );
        sendTextReply(target, logContext, request.getReply());
        sendImageReplies(target, logContext, request.getImages());
        sendVoiceReplies(target, logContext, request.getVoices());
        return ApiResult.success(null);
    }

    /**
     * Handle downstream callback replies. Currently supports text, image and voice replies.
     */
    public void sendReplyToCustomer(WxMessageInfo wxMessageInfo, WxUserInfo receiverUser, String callbackBody) {
        if (wxMessageInfo == null || receiverUser == null) {
            logger.warn("Reply context is missing. wxMessageInfo={}, receiverUser={}", wxMessageInfo, receiverUser);
            return;
        }
        if (!StringUtils.hasText(callbackBody)) {
            logger.warn("Callback body is empty, skip reply. msgId={}", wxMessageInfo.getMsgId());
            return;
        }

        CustomerReplyCallbackResult callbackResult = parseCallbackResult(callbackBody);
        if (callbackResult == null || !callbackResult.isSuccess()) {
            logger.warn("Callback result is not ok, skip reply. msgId={}, body={}", wxMessageInfo.getMsgId(), callbackBody);
            return;
        }

        if (!StringUtils.hasText(wxMessageInfo.getRoomId())) {
            ApiResult<Void> result = sendFriendReply(buildFriendReplyRequest(wxMessageInfo, receiverUser, callbackResult));
            if (result != null && result.getCode() != 0) {
                logger.warn(
                        "Failed to send direct friend reply. msgId={}, sender={}, receiver={}, msg={}",
                        wxMessageInfo.getMsgId(),
                        wxMessageInfo.getReceiver(),
                        wxMessageInfo.getSender(),
                        result.getMsg()
                );
            }
            return;
        }

        ReplyTarget target = resolveReplyTarget(wxMessageInfo, receiverUser);
        if (target == null) {
            return;
        }

        ReplyLogContext logContext = ReplyLogContext.fromMessage(
                wxMessageInfo.getMsgId(),
                wxMessageInfo.getSender(),
                wxMessageInfo.getReceiver()
        );
        sendTextReply(target, logContext, callbackResult.getReply());
        sendImageReplies(target, logContext, callbackResult.getImages());
        sendVoiceReplies(target, logContext, callbackResult.getVoices());
    }

    /**
     * Send reply message to a direct friend with explicit sender / receiver parameters only.
     */
    public ApiResult<Void> sendFriendReply(SendFriendReplyRequest request) {
        String validationMessage = validateFriendReplyRequest(request);
        if (validationMessage != null) {
            return ApiResult.fail(validationMessage);
        }

        try {
            ReplyTarget target = new ReplyTarget(request.getUuid(), request.getReceiver(), false, request.getKfId());
            ReplyLogContext logContext = ReplyLogContext.fromMessage(null, request.getSender(), request.getReceiver());
            sendTextReply(target, logContext, request.getReply());
            sendImageReplies(target, logContext, request.getImages());
            sendVoiceReplies(target, logContext, request.getVoices());
            return ApiResult.success(null);
        } catch (Exception e) {
            logger.error(
                    "Failed to send friend reply. sender={}, receiver={}",
                    request.getSender(),
                    request.getReceiver(),
                    e
            );
            return ApiResult.fail("send friend reply failed");
        }
    }

    private ReplyTarget resolveReplyTargetFromCallback(WecomAgentReplyCallbackRequest request) {
        if (request == null) {
            return null;
        }
        Long senderUserId = resolveCallbackAccountUserId(request);
        String uuid = request.getUuid();
        if (!StringUtils.hasText(uuid) && senderUserId != null) {
            WxUserInfo userInfo = wxUserInfoService.selectByUserId(senderUserId);
            if (userInfo != null) {
                uuid = userInfo.getUuid();
            }
        }
        if (!StringUtils.hasText(uuid)) {
            logger.warn("Reply callback target uuid is empty and cannot be resolved. request={}", JSON.toJSONString(request));
            return null;
        }

        boolean isRoomMessage = Boolean.TRUE.equals(request.getIsRoom());
        Long sendUserid = isRoomMessage
                ? parseLongSafely(request.getRoomId())
                : resolveCallbackReplyReceiver(request);
        if (sendUserid == null) {
            logger.warn("Reply callback target receiver is invalid. request={}", JSON.toJSONString(request));
            return null;
        }

        return new ReplyTarget(
                uuid,
                sendUserid,
                isRoomMessage,
                request.getKfId()
        );
    }

    private Long resolveCallbackReplySender(WecomAgentReplyCallbackRequest request) {
        if (request == null) {
            return null;
        }
        return firstNonNullLong(
                request.getReplySender(),
                request.getSender(),
                request.getReplyAccountUserId(),
                request.getAccountUserId(),
                request.getReceiverUserId()
        );
    }

    private Long resolveCallbackReplyReceiver(WecomAgentReplyCallbackRequest request) {
        if (request == null) {
            return null;
        }
        return firstNonNullLong(
                request.getReplyReceiver(),
                request.getReceiver()
        );
    }

    private Long resolveCallbackAccountUserId(WecomAgentReplyCallbackRequest request) {
        if (request == null) {
            return null;
        }
        return firstNonNullLong(
                request.getReplyAccountUserId(),
                request.getReplySender(),
                request.getSender(),
                request.getAccountUserId(),
                request.getReceiverUserId()
        );
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
    private void sendTextReply(ReplyTarget target, ReplyLogContext logContext, String reply) {
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
        logSendResult("text", logContext, sendResult);
    }

    /**
     * Upload and send all callback images.
     */
    private void sendImageReplies(ReplyTarget target, ReplyLogContext logContext, List<ReplyMediaItem> imageItems) {
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
                logSendResult("image", logContext, sendResult);
            } catch (Exception e) {
                logger.error(
                        "Failed to send reply image. msgId={}, sender={}, receiver={}, imageIndex={}, payload={}",
                        logContext.getMsgId(),
                        logContext.getSender(),
                        logContext.getReceiver(),
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
    private void sendVoiceReplies(ReplyTarget target, ReplyLogContext logContext, List<ReplyMediaItem> voiceItems) {
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
                logSendResult("voice", logContext, sendResult);
            } catch (Exception e) {
                logger.error(
                        "Failed to send reply voice. msgId={}, sender={}, receiver={}, voiceIndex={}, payload={}",
                        logContext.getMsgId(),
                        logContext.getSender(),
                        logContext.getReceiver(),
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
        mediaItem.setContentType(firstNonBlank(
                payload.getString("contentType"),
                payload.getString("content_type"),
                payload.getString("mimeType"),
                payload.getString("mime_type")
        ));
        mediaItem.setIsHd(firstNonNullInteger(payload.getInteger("is_hd"), payload.getInteger("isHd")));
        mediaItem.setVoiceTime(firstNonNullInteger(
                payload.getInteger("voice_time"),
                payload.getInteger("voiceTime"),
                payload.getInteger("voice_duration"),
                payload.getInteger("voiceDuration"),
                payload.getInteger("duration")
        ));
        enrichMediaItem(mediaItem, mediaType);
        return mediaItem;
    }

    /**
     * Fill type specific defaults. Voice reply keeps the extension aligned with the audio format.
     */
    private void enrichMediaItem(ReplyMediaItem mediaItem, String mediaType) {
        if ("voice".equals(mediaType)) {
            mediaItem.ensureFilename(buildDefaultVoiceFilename(mediaItem));
        }
    }

    private String buildDefaultVoiceFilename(ReplyMediaItem mediaItem) {
        String extension = resolveVoiceExtension(mediaItem);
        if (!StringUtils.hasText(extension)) {
            extension = "silk";
        }
        return "reply-" + System.currentTimeMillis() + "." + extension;
    }

    private String resolveVoiceExtension(ReplyMediaItem mediaItem) {
        String contentType = firstNonBlank(
                mediaItem == null ? null : mediaItem.getContentType(),
                extractContentTypeFromDataUrl(mediaItem == null ? null : mediaItem.getBase64())
        );
        if (StringUtils.hasText(contentType)) {
            String mimeExtension = resolveExtensionFromContentType(contentType);
            if (StringUtils.hasText(mimeExtension)) {
                return mimeExtension;
            }
        }

        if (mediaItem != null && StringUtils.hasText(mediaItem.getUrl())) {
            String cleanUrl = mediaItem.getUrl();
            int queryIndex = cleanUrl.indexOf('?');
            if (queryIndex >= 0) {
                cleanUrl = cleanUrl.substring(0, queryIndex);
            }
            String extension = StringUtils.getFilenameExtension(cleanUrl);
            if (StringUtils.hasText(extension)) {
                return extension.toLowerCase(Locale.ROOT);
            }
        }
        return null;
    }

    private String extractContentTypeFromDataUrl(String base64Payload) {
        if (!StringUtils.hasText(base64Payload)) {
            return null;
        }
        String trimmed = base64Payload.trim();
        int commaIndex = trimmed.indexOf(',');
        if (!trimmed.startsWith("data:") || commaIndex <= 5) {
            return null;
        }
        String meta = trimmed.substring(5, commaIndex);
        int semicolonIndex = meta.indexOf(';');
        return semicolonIndex >= 0 ? meta.substring(0, semicolonIndex) : meta;
    }

    private String resolveExtensionFromContentType(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return null;
        }
        String normalized = contentType.trim().toLowerCase(Locale.ROOT);
        if ("audio/mpeg".equals(normalized) || "audio/mp3".equals(normalized) || "audio/x-mp3".equals(normalized)) {
            return "mp3";
        }
        if ("audio/mp4".equals(normalized) || "audio/x-m4a".equals(normalized)) {
            return "m4a";
        }
        if ("audio/wav".equals(normalized) || "audio/x-wav".equals(normalized) || "audio/wave".equals(normalized)) {
            return "wav";
        }
        if ("audio/amr".equals(normalized)) {
            return "amr";
        }
        if ("audio/silk".equals(normalized) || "application/silk".equals(normalized)) {
            return "silk";
        }
        if ("audio/ogg".equals(normalized)) {
            return "ogg";
        }
        if ("audio/webm".equals(normalized)) {
            return "webm";
        }
        return null;
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
        Integer inferredDuration = inferVoiceDuration(voicePayload);
        if (inferredDuration != null) {
            voicePayload.setVoiceTime(inferredDuration);
            return inferredDuration;
        }
        throw new IllegalStateException("Reply voice is missing voice_time");
    }

    private Integer inferVoiceDuration(ReplyMediaItem voicePayload) {
        if (voicePayload == null || !StringUtils.hasText(voicePayload.getBase64())) {
            return null;
        }
        byte[] audioBytes = decodeBase64Bytes(voicePayload.getBase64());
        if (audioBytes == null || audioBytes.length == 0) {
            return null;
        }

        Integer wavDuration = inferWavDuration(audioBytes);
        if (wavDuration != null) {
            return wavDuration;
        }

        Integer mp3Duration = inferMp3Duration(audioBytes);
        if (mp3Duration != null) {
            return mp3Duration;
        }
        return null;
    }

    private byte[] decodeBase64Bytes(String base64Payload) {
        String trimmed = base64Payload == null ? "" : base64Payload.trim();
        String encoded = trimmed;
        int commaIndex = trimmed.indexOf(',');
        if (trimmed.startsWith("data:") && commaIndex > 0) {
            encoded = trimmed.substring(commaIndex + 1);
        }

        String normalized = normalizeBase64(encoded);
        try {
            return Base64.getDecoder().decode(normalized);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to decode base64 voice payload for duration inference", e);
            return null;
        }
    }

    private String normalizeBase64(String encoded) {
        String normalized = encoded == null ? "" : encoded.replaceAll("\\s", "");
        int remainder = normalized.length() % 4;
        if (remainder == 0) {
            return normalized;
        }
        StringBuilder builder = new StringBuilder(normalized);
        for (int i = remainder; i < 4; i++) {
            builder.append('=');
        }
        return builder.toString();
    }

    private Integer inferWavDuration(byte[] bytes) {
        if (bytes.length < 44) {
            return null;
        }
        if (!startsWith(bytes, "RIFF") || !matchesAt(bytes, 8, "WAVE")) {
            return null;
        }

        int offset = 12;
        Integer dataSize = null;
        Integer byteRate = null;
        while (offset + 8 <= bytes.length) {
            String chunkId = new String(bytes, offset, 4, StandardCharsets.US_ASCII);
            int chunkSize = readLittleEndianInt(bytes, offset + 4);
            int nextOffset = offset + 8 + chunkSize + (chunkSize % 2);
            if (chunkSize < 0 || nextOffset > bytes.length + 1) {
                return null;
            }

            if ("fmt ".equals(chunkId) && chunkSize >= 16 && offset + 16 + 8 <= bytes.length) {
                byteRate = readLittleEndianInt(bytes, offset + 16);
            } else if ("data".equals(chunkId)) {
                dataSize = chunkSize;
            }

            if (dataSize != null && byteRate != null && byteRate > 0) {
                return Math.max(1, (int) Math.ceil((double) dataSize / (double) byteRate));
            }
            offset = nextOffset;
        }
        return null;
    }

    private Integer inferMp3Duration(byte[] bytes) {
        int offset = skipId3v2Tag(bytes);
        long totalSamples = 0L;
        int frameCount = 0;

        while (offset + 4 <= bytes.length) {
            int header = readBigEndianInt(bytes, offset);
            Mp3Frame frame = parseMp3Frame(header);
            if (frame == null) {
                offset++;
                continue;
            }

            if (frame.frameLength <= 0 || offset + frame.frameLength > bytes.length) {
                break;
            }

            totalSamples += frame.samplesPerFrame;
            frameCount++;
            offset += frame.frameLength;
        }

        if (frameCount == 0) {
            return null;
        }

        int durationSeconds = (int) Math.ceil((double) totalSamples / (double) 44100D);
        Mp3Frame firstFrame = findFirstMp3Frame(bytes, skipId3v2Tag(bytes));
        if (firstFrame != null && firstFrame.sampleRate > 0) {
            durationSeconds = (int) Math.ceil((double) totalSamples / (double) firstFrame.sampleRate);
        }
        return Math.max(1, durationSeconds);
    }

    private Mp3Frame findFirstMp3Frame(byte[] bytes, int startOffset) {
        for (int offset = startOffset; offset + 4 <= bytes.length; offset++) {
            Mp3Frame frame = parseMp3Frame(readBigEndianInt(bytes, offset));
            if (frame != null) {
                return frame;
            }
        }
        return null;
    }

    private int skipId3v2Tag(byte[] bytes) {
        if (bytes.length < 10 || !startsWith(bytes, "ID3")) {
            return 0;
        }
        int size = ((bytes[6] & 0x7F) << 21)
                | ((bytes[7] & 0x7F) << 14)
                | ((bytes[8] & 0x7F) << 7)
                | (bytes[9] & 0x7F);
        int tagSize = 10 + size;
        if (tagSize > bytes.length) {
            return 0;
        }
        return tagSize;
    }

    private Mp3Frame parseMp3Frame(int header) {
        if ((header & 0xFFE00000) != 0xFFE00000) {
            return null;
        }

        int versionBits = (header >>> 19) & 0x3;
        int layerBits = (header >>> 17) & 0x3;
        int bitrateIndex = (header >>> 12) & 0xF;
        int sampleRateIndex = (header >>> 10) & 0x3;
        int paddingBit = (header >>> 9) & 0x1;

        if (versionBits == 1 || layerBits != 1 || bitrateIndex == 0 || bitrateIndex == 15 || sampleRateIndex == 3) {
            return null;
        }

        int sampleRate = resolveMp3SampleRate(versionBits, sampleRateIndex);
        int bitrate = resolveMp3Bitrate(versionBits, bitrateIndex);
        if (sampleRate <= 0 || bitrate <= 0) {
            return null;
        }

        int frameLength;
        int samplesPerFrame;
        if (versionBits == 3) {
            frameLength = (144 * bitrate * 1000) / sampleRate + paddingBit;
            samplesPerFrame = 1152;
        } else {
            frameLength = (72 * bitrate * 1000) / sampleRate + paddingBit;
            samplesPerFrame = 576;
        }

        if (frameLength <= 4) {
            return null;
        }
        return new Mp3Frame(frameLength, sampleRate, samplesPerFrame);
    }

    private int resolveMp3SampleRate(int versionBits, int sampleRateIndex) {
        int[] baseRates = new int[]{44100, 48000, 32000};
        int sampleRate = baseRates[sampleRateIndex];
        if (versionBits == 2) {
            return sampleRate / 2;
        }
        if (versionBits == 0) {
            return sampleRate / 4;
        }
        return sampleRate;
    }

    private int resolveMp3Bitrate(int versionBits, int bitrateIndex) {
        int[] mpeg1Layer3 = new int[]{0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 0};
        int[] mpeg2Layer3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, 0};
        if (versionBits == 3) {
            return mpeg1Layer3[bitrateIndex];
        }
        return mpeg2Layer3[bitrateIndex];
    }

    private boolean startsWith(byte[] bytes, String value) {
        return matchesAt(bytes, 0, value);
    }

    private boolean matchesAt(byte[] bytes, int offset, String value) {
        byte[] ascii = value.getBytes(StandardCharsets.US_ASCII);
        if (offset < 0 || bytes.length < offset + ascii.length) {
            return false;
        }
        for (int i = 0; i < ascii.length; i++) {
            if (bytes[offset + i] != ascii[i]) {
                return false;
            }
        }
        return true;
    }

    private int readLittleEndianInt(byte[] bytes, int offset) {
        if (offset + 4 > bytes.length) {
            return -1;
        }
        return (bytes[offset] & 0xFF)
                | ((bytes[offset + 1] & 0xFF) << 8)
                | ((bytes[offset + 2] & 0xFF) << 16)
                | ((bytes[offset + 3] & 0xFF) << 24);
    }

    private int readBigEndianInt(byte[] bytes, int offset) {
        if (offset + 4 > bytes.length) {
            return -1;
        }
        return ((bytes[offset] & 0xFF) << 24)
                | ((bytes[offset + 1] & 0xFF) << 16)
                | ((bytes[offset + 2] & 0xFF) << 8)
                | (bytes[offset + 3] & 0xFF);
    }

    private static class Mp3Frame {
        private final int frameLength;
        private final int sampleRate;
        private final int samplesPerFrame;

        private Mp3Frame(int frameLength, int sampleRate, int samplesPerFrame) {
            this.frameLength = frameLength;
            this.sampleRate = sampleRate;
            this.samplesPerFrame = samplesPerFrame;
        }
    }

    /**
     * Log reply sending result for troubleshooting.
     */
    private void logSendResult(String replyType, ReplyLogContext logContext, ApiResult<SendMsgResponse> sendResult) {
        logger.info(
                "Reply send finished. type={}, msgId={}, receiver={}, sender={}, code={}, msg={}",
                replyType,
                logContext.getMsgId(),
                logContext.getReceiver(),
                logContext.getSender(),
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

    /**
     * Return the first non-null long from alias candidates.
     */
    private Long firstNonNullLong(Long... values) {
        if (values == null) {
            return null;
        }
        for (Long value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private SendFriendReplyRequest buildFriendReplyRequest(WxMessageInfo wxMessageInfo,
                                                           WxUserInfo receiverUser,
                                                           CustomerReplyCallbackResult callbackResult) {
        return SendFriendReplyRequest.builder()
                .uuid(receiverUser.getUuid())
                .sender(wxMessageInfo.getReceiver())
                .receiver(wxMessageInfo.getSender())
                .kfId(wxMessageInfo.getKfId())
                .reply(callbackResult.getReply())
                .images(callbackResult.getImages())
                .voices(callbackResult.getVoices())
                .build();
    }

    private String validateFriendReplyRequest(SendFriendReplyRequest request) {
        if (request == null) {
            return "request is null";
        }
        if (!StringUtils.hasText(request.getUuid())) {
            return "uuid is required";
        }
        if (request.getSender() == null) {
            return "sender is required";
        }
        if (request.getReceiver() == null) {
            return "receiver is required";
        }
        if (!StringUtils.hasText(request.getReply())
                && (request.getImages() == null || request.getImages().isEmpty())
                && (request.getVoices() == null || request.getVoices().isEmpty())) {
            return "reply or media is required";
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

    private static class ReplyLogContext {
        private final Long msgId;
        private final Long sender;
        private final Long receiver;

        private ReplyLogContext(Long msgId, Long sender, Long receiver) {
            this.msgId = msgId;
            this.sender = sender;
            this.receiver = receiver;
        }

        private static ReplyLogContext fromMessage(Long msgId, Long sender, Long receiver) {
            return new ReplyLogContext(msgId, sender, receiver);
        }

        public Long getMsgId() {
            return msgId;
        }

        public Long getSender() {
            return sender;
        }

        public Long getReceiver() {
            return receiver;
        }
    }
}
