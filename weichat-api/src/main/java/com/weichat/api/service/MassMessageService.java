package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.util.MessageTemplateUtil;
import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.request.mass.MassTaskAppPayload;
import com.weichat.api.vo.request.mass.MassTaskLinkPayload;
import com.weichat.api.vo.request.mass.MassTaskMediaPayload;
import com.weichat.api.vo.request.mass.MassTaskPayload;
import com.weichat.api.vo.request.message.SendAppRequest;
import com.weichat.api.vo.request.message.SendFileRequest;
import com.weichat.api.vo.request.message.SendImageRequest;
import com.weichat.api.vo.request.message.SendLinkRequest;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.request.message.SendVideoRequest;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.enums.MassTaskDetailSendStatusEnum;
import com.weichat.common.enums.MassTaskDetailSentFlagEnum;
import com.weichat.common.enums.MassTaskReceiverTypeEnum;
import com.weichat.common.service.MassTaskDetailService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.MessageTemplateService;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MassMessageService {

    private static final int MSG_TYPE_TEXT = 0;
    private static final int MSG_TYPE_IMAGE = 1;
    private static final int MSG_TYPE_FILE = 2;
    private static final int MSG_TYPE_VOICE = 3;
    private static final int MSG_TYPE_VIDEO = 4;
    private static final int MSG_TYPE_LINK = 5;
    private static final int MSG_TYPE_APP = 6;

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired
    private CdnImageService cdnImageService;

    @Autowired
    private CdnFileService cdnFileService;

    private Map<Integer, MassTaskMessageSender> messageSenders = Collections.emptyMap();

    @PostConstruct
    private void initMessageSenders() {
        Map<Integer, MassTaskMessageSender> senders = new HashMap<>();
        senders.put(MSG_TYPE_TEXT, this::sendTextMessage);
        senders.put(MSG_TYPE_IMAGE, this::sendImageMessage);
        senders.put(MSG_TYPE_FILE, this::sendFileMessage);
        senders.put(MSG_TYPE_VOICE, this::sendVoiceMessage);
        senders.put(MSG_TYPE_VIDEO, this::sendVideoMessage);
        senders.put(MSG_TYPE_LINK, this::sendLinkMessage);
        senders.put(MSG_TYPE_APP, this::sendAppMessage);
        this.messageSenders = Collections.unmodifiableMap(senders);
    }

    public boolean sendMassMessageToReceiver(MassTaskDetail detail) {
        try {
            MassTask task = massTaskService.getMassTaskById(detail.getTaskId());
            if (task == null) {
                markFailure(detail, "mass task not found");
                return false;
            }

            ReceiverContext receiverContext = resolveReceiverContext(detail);
            if (receiverContext == null) {
                return false;
            }

            MassTaskMessageSender sender = messageSenders.get(task.getMsgType());
            if (sender == null) {
                log.error("unsupported msgType: {}", task.getMsgType());
                markFailure(detail, "unsupported msgType: " + task.getMsgType());
                return false;
            }

            JSONObject result = sender.send(task, receiverContext);
            return handleSendResult(detail, result);
        } catch (Exception e) {
            log.error("mass message send failed, detailId={}", detail.getId(), e);
            markFailure(detail, e.getMessage());
            return false;
        }
    }

    private ReceiverContext resolveReceiverContext(MassTaskDetail detail) {
        if (MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode().equals(detail.getReceiverType())) {
            return resolveExternalContact(detail);
        }
        if (MassTaskReceiverTypeEnum.GROUP_CHAT.getCode().equals(detail.getReceiverType())) {
            return resolveGroupChat(detail);
        }
        markFailure(detail, "unsupported receiverType: " + detail.getReceiverType());
        return null;
    }

    private ReceiverContext resolveExternalContact(MassTaskDetail detail) {
        WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(detail.getReceiverId());
        if (friendInfo == null) {
            markFailure(detail, "friend not found");
            return null;
        }

        WxUserInfo senderUserInfo = resolveSenderUserInfo(friendInfo.getOwnerUserId());
        if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
            markFailure(detail, "sender uuid not found");
            return null;
        }

        return new ReceiverContext(
                senderUserInfo.getUuid(),
                friendInfo.getUserId(),
                false,
                resolveFriendName(friendInfo)
        );
    }

    private ReceiverContext resolveGroupChat(MassTaskDetail detail) {
        WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(detail.getReceiverId());
        if (groupInfo == null) {
            markFailure(detail, "group not found");
            return null;
        }

        WxUserInfo senderUserInfo = resolveSenderUserInfo(groupInfo.getCreateUserId());
        if (senderUserInfo == null || !StringUtils.hasText(senderUserInfo.getUuid())) {
            markFailure(detail, "sender uuid not found");
            return null;
        }

        Long roomId = parseLongSafely(groupInfo.getRoomId());
        if (roomId == null) {
            markFailure(detail, "invalid roomId");
            return null;
        }

        return new ReceiverContext(
                senderUserInfo.getUuid(),
                roomId,
                true,
                groupInfo.getNickname()
        );
    }

    private JSONObject sendTextMessage(MassTask task, ReceiverContext receiverContext) {
        SendTextRequest request = SendTextRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .content(resolveContent(task, receiverContext.getReceiverName()))
                .build();
        return postMessage("/wxwork/SendTextMsg", request);
    }

    private JSONObject sendImageMessage(MassTask task, ReceiverContext receiverContext) {
        MediaMaterial material = resolveMediaMaterial(task, task.getImageMediaId(), "image material is empty");
        SendImageRequest request;
        if (material.hasSourcePayload()) {
            CdnUploadResponse uploadResponse = validateImageUploadResponse(
                    cdnImageService.uploadImage(receiverContext.getSenderUuid(), material.toReplyMediaItem())
            );
            request = SendImageRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(uploadResponse.getCdn_key())
                    .aeskey(uploadResponse.getAes_key())
                    .md5(uploadResponse.getMd5())
                    .fileSize(uploadResponse.getSize())
                    .width(firstNonNull(material.getWidth(), uploadResponse.getWidth()))
                    .height(firstNonNull(material.getHeight(), uploadResponse.getHeight()))
                    .thumb_image_height(firstNonNull(material.getThumbImageHeight(), uploadResponse.getThumb_image_height()))
                    .thumb_image_width(firstNonNull(material.getThumbImageWidth(), uploadResponse.getThumb_image_width()))
                    .thumb_file_size(firstNonNull(material.getThumbFileSize(), uploadResponse.getThumb_file_size()))
                    .thumb_file_md5(firstNonBlank(material.getThumbFileMd5(), uploadResponse.getThumb_file_md5()))
                    .is_hd(material.getIsHd())
                    .build();
        } else {
            request = SendImageRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(requireText(material.getCdnkey(), "image cdnkey is required"))
                    .aeskey(requireText(material.getAeskey(), "image aeskey is required"))
                    .md5(requireText(material.getMd5(), "image md5 is required"))
                    .fileSize(requireInteger(material.getFileSize(), "image fileSize is required"))
                    .width(material.getWidth())
                    .height(material.getHeight())
                    .thumb_image_height(material.getThumbImageHeight())
                    .thumb_image_width(material.getThumbImageWidth())
                    .thumb_file_size(material.getThumbFileSize())
                    .thumb_file_md5(material.getThumbFileMd5())
                    .is_hd(material.getIsHd())
                    .build();
        }
        return postMessage("/wxwork/SendCDNImgMsg", request);
    }

    private JSONObject sendFileMessage(MassTask task, ReceiverContext receiverContext) {
        MediaMaterial material = resolveMediaMaterial(task, task.getFileMediaId(), "file material is empty");
        SendFileRequest request;
        if (material.hasSourcePayload()) {
            CdnUploadResponse uploadResponse = validateFileUploadResponse(
                    cdnFileService.uploadFile(receiverContext.getSenderUuid(), material.toReplyMediaItem())
            );
            request = SendFileRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(resolveCdnKey(uploadResponse))
                    .aeskey(uploadResponse.getAes_key())
                    .md5(uploadResponse.getMd5())
                    .file_name(resolveFileName(material))
                    .fileSize(uploadResponse.getSize())
                    .build();
        } else {
            request = SendFileRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(requireText(resolveMaterialCdnKey(material), "file cdnkey is required"))
                    .aeskey(requireText(material.getAeskey(), "file aeskey is required"))
                    .md5(requireText(material.getMd5(), "file md5 is required"))
                    .file_name(requireText(resolveFileName(material), "file name is required"))
                    .fileSize(requireInteger(material.getFileSize(), "fileSize is required"))
                    .build();
        }
        return postMessage("/wxwork/SendCDNFileMsg", request);
    }

    private JSONObject sendVoiceMessage(MassTask task, ReceiverContext receiverContext) {
        MediaMaterial material = resolveMediaMaterial(task, task.getAudioMediaId(), "voice material is empty");
        SendVoiceRequest request;
        if (material.hasSourcePayload()) {
            CdnUploadResponse uploadResponse = validateFileUploadResponse(
                    cdnFileService.uploadFile(receiverContext.getSenderUuid(), material.toReplyMediaItem())
            );
            request = SendVoiceRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(resolveCdnKey(uploadResponse))
                    .aeskey(uploadResponse.getAes_key())
                    .md5(uploadResponse.getMd5())
                    .voice_time(requireInteger(material.getVoiceTime(), "voice_time is required"))
                    .fileSize(uploadResponse.getSize())
                    .build();
        } else {
            request = SendVoiceRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(requireText(resolveMaterialCdnKey(material), "voice cdnkey is required"))
                    .aeskey(requireText(material.getAeskey(), "voice aeskey is required"))
                    .md5(requireText(material.getMd5(), "voice md5 is required"))
                    .voice_time(requireInteger(material.getVoiceTime(), "voice_time is required"))
                    .fileSize(requireInteger(material.getFileSize(), "voice fileSize is required"))
                    .build();
        }
        return postMessage("/wxwork/SendCDNVoiceMsg", request);
    }

    private JSONObject sendVideoMessage(MassTask task, ReceiverContext receiverContext) {
        MediaMaterial material = resolveMediaMaterial(task, task.getVideoMediaId(), "video material is empty");
        SendVideoRequest request;
        if (material.hasSourcePayload()) {
            CdnUploadResponse uploadResponse = validateFileUploadResponse(
                    cdnFileService.uploadFile(receiverContext.getSenderUuid(), material.toReplyMediaItem())
            );
            request = SendVideoRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(resolveCdnKey(uploadResponse))
                    .aeskey(uploadResponse.getAes_key())
                    .md5(uploadResponse.getMd5())
                    .video_duration(requireInteger(material.getVideoDuration(), "video_duration is required"))
                    .video_width(firstNonNull(material.getVideoWidth(), uploadResponse.getWidth()))
                    .video_height(firstNonNull(material.getVideoHeight(), uploadResponse.getHeight()))
                    .fileSize(uploadResponse.getSize())
                    .build();
        } else {
            request = SendVideoRequest.builder()
                    .uuid(receiverContext.getSenderUuid())
                    .send_userid(receiverContext.getReceiverUserId())
                    .isRoom(receiverContext.isRoomMessage())
                    .cdnkey(requireText(resolveMaterialCdnKey(material), "video cdnkey is required"))
                    .aeskey(requireText(material.getAeskey(), "video aeskey is required"))
                    .md5(requireText(material.getMd5(), "video md5 is required"))
                    .video_duration(requireInteger(material.getVideoDuration(), "video_duration is required"))
                    .video_width(material.getVideoWidth())
                    .video_height(material.getVideoHeight())
                    .fileSize(requireInteger(material.getFileSize(), "video fileSize is required"))
                    .build();
        }
        return postMessage("/wxwork/SendCDNVideoMsg", request);
    }

    private JSONObject sendLinkMessage(MassTask task, ReceiverContext receiverContext) {
        MassTaskLinkPayload payload = resolveLinkPayload(task);
        SendLinkRequest request = SendLinkRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .url(requireText(payload.getUrl(), "link url is required"))
                .title(requireText(payload.getTitle(), "link title is required"))
                .content(requireText(payload.getContent(), "link content is required"))
                .imgurl(payload.getImgurl())
                .build();
        return postMessage("/wxwork/SendLinkMsg", request);
    }

    private JSONObject sendAppMessage(MassTask task, ReceiverContext receiverContext) {
        AppMessageMaterial appMaterial = resolveAppMessageMaterial(task);
        MediaMaterial coverMaterial = appMaterial.getCoverMaterial();
        if (coverMaterial == null) {
            throw new IllegalArgumentException("app cover is required");
        }

        CdnUploadResponse uploadResponse = coverMaterial.hasSourcePayload()
                ? validateFileUploadResponse(cdnFileService.uploadFile(receiverContext.getSenderUuid(), coverMaterial.toReplyMediaItem()))
                : buildUploadResponseFromMaterial(coverMaterial);

        SendAppRequest request = SendAppRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .title(requireText(appMaterial.getTitle(), "app title is required"))
                .desc(requireText(appMaterial.getDesc(), "app desc is required"))
                .appName(requireText(appMaterial.getAppName(), "appName is required"))
                .appid(requireText(appMaterial.getAppid(), "appid is required"))
                .username(requireText(appMaterial.getUsername(), "username is required"))
                .pagepath(requireText(appMaterial.getPagepath(), "pagepath is required"))
                .weappIconUrl(appMaterial.getWeappIconUrl())
                .cdnkey(requireText(resolveCdnKey(uploadResponse), "app cover cdnkey is required"))
                .aeskey(requireText(uploadResponse.getAes_key(), "app cover aeskey is required"))
                .md5(requireText(uploadResponse.getMd5(), "app cover md5 is required"))
                .fileSize(requireInteger(uploadResponse.getSize(), "app cover fileSize is required"))
                .build();
        return postMessage("/wxwork/SendAppMsg", request);
    }

    private JSONObject postMessage(String endpoint, Object request) {
        return wxWorkApiClient.post(endpoint, JSON.parseObject(JSON.toJSONString(request)));
    }

    private boolean handleSendResult(MassTaskDetail detail, JSONObject result) {
        if (result == null) {
            markFailure(detail, "downstream response is empty");
            return false;
        }
        Integer code = resolveResponseCode(result);
        if (code != null && code == 0) {
            markSuccess(detail);
            return true;
        }
        markFailure(detail, firstNonBlank(resolveResponseMessage(result), "send failed"));
        return false;
    }

    private String resolveContent(MassTask task, String receiverName) {
        if (task.getTemplateId() == null) {
            return task.getContent();
        }
        MessageTemplate template = messageTemplateService.getTemplateById(task.getTemplateId());
        if (template == null) {
            return task.getContent();
        }
        return MessageTemplateUtil.renderTemplate(template.getTemplateContent(), receiverName);
    }

    private void markSuccess(MassTaskDetail detail) {
        massTaskDetailService.updateSendSuccessStatus(detail.getId());
        updateTaskStatistics(detail.getTaskId());
    }

    private void markFailure(MassTaskDetail detail, String message) {
        massTaskDetailService.updateSendFailureStatus(detail.getId(), firstNonBlank(message, "send failed"));
        updateTaskStatistics(detail.getTaskId());
    }

    private void updateTaskStatistics(Long taskId) {
        MassTask task = massTaskService.getMassTaskById(taskId);
        if (task == null) {
            return;
        }
        List<MassTaskDetail> details = massTaskDetailService.getDetailsByTaskId(taskId);
        int sent = 0;
        int success = 0;
        for (MassTaskDetail detail : details) {
            if (MassTaskDetailSentFlagEnum.SENT.getCode().equals(detail.getIsSent())) {
                sent++;
                if (MassTaskDetailSendStatusEnum.SUCCESS.getCode().equals(detail.getSendStatus())) {
                    success++;
                }
            }
        }
        massTaskService.updateTaskStatistics(taskId, sent, success);
    }

    private MassTaskPayload resolveTaskPayload(MassTask task) {
        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
        return json == null ? null : json.toJavaObject(MassTaskPayload.class);
    }

    private MediaMaterial resolveMediaMaterial(MassTask task, String rawMaterial, String emptyMessage) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getMedia() != null) {
            return MediaMaterial.fromPayload(payload.getMedia());
        }
        JSONObject json = parseMaterialObject(rawMaterial);
        if (json == null) {
            throw new IllegalArgumentException(emptyMessage);
        }
        return MediaMaterial.fromJson(json);
    }

    private MassTaskLinkPayload resolveLinkPayload(MassTask task) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getLink() != null) {
            return payload.getLink();
        }
        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
        if (json == null) {
            throw new IllegalArgumentException("link payload is empty");
        }
        JSONObject linkJson = json.getJSONObject("link");
        return (linkJson == null ? json : linkJson).toJavaObject(MassTaskLinkPayload.class);
    }

    private AppMessageMaterial resolveAppMessageMaterial(MassTask task) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getApp() != null) {
            return AppMessageMaterial.fromPayload(payload.getApp());
        }
        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
        if (json == null) {
            throw new IllegalArgumentException("app payload is empty");
        }
        JSONObject appJson = json.getJSONObject("app");
        return AppMessageMaterial.fromJson(appJson == null ? json : appJson);
    }

    private JSONObject parseMaterialObject(String rawMaterial) {
        if (!StringUtils.hasText(rawMaterial)) {
            return null;
        }
        String trimmed = rawMaterial.trim();
        if (trimmed.startsWith("{")) {
            return JSON.parseObject(trimmed);
        }
        JSONObject json = new JSONObject();
        if (isUrl(trimmed)) {
            json.put("url", trimmed);
        } else if (isDataUrl(trimmed)) {
            json.put("base64", trimmed);
        } else {
            json.put("cdnkey", trimmed);
        }
        return json;
    }

    private JSONObject firstStructuredObject(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String candidate : candidates) {
            if (!StringUtils.hasText(candidate)) {
                continue;
            }
            String trimmed = candidate.trim();
            if (!trimmed.startsWith("{")) {
                continue;
            }
            try {
                return JSON.parseObject(trimmed);
            } catch (Exception e) {
                log.warn("failed to parse structured payload: " + trimmed, e);
            }
        }
        return null;
    }

    private boolean isUrl(String value) {
        return value.startsWith("http://") || value.startsWith("https://");
    }

    private boolean isDataUrl(String value) {
        return value.startsWith("data:") || value.contains(";base64,");
    }

    private CdnUploadResponse validateImageUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null
                || !StringUtils.hasText(uploadResponse.getCdn_key())
                || !StringUtils.hasText(uploadResponse.getAes_key())
                || !StringUtils.hasText(uploadResponse.getMd5())
                || uploadResponse.getSize() == null) {
            throw new IllegalStateException("image upload response is incomplete");
        }
        return uploadResponse;
    }

    private CdnUploadResponse validateFileUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null
                || !StringUtils.hasText(resolveCdnKey(uploadResponse))
                || !StringUtils.hasText(uploadResponse.getAes_key())
                || !StringUtils.hasText(uploadResponse.getMd5())
                || uploadResponse.getSize() == null) {
            throw new IllegalStateException("file upload response is incomplete");
        }
        return uploadResponse;
    }

    private CdnUploadResponse buildUploadResponseFromMaterial(MediaMaterial material) {
        CdnUploadResponse uploadResponse = new CdnUploadResponse();
        uploadResponse.setCdn_key(resolveMaterialCdnKey(material));
        uploadResponse.setFileid(material.getFileId());
        uploadResponse.setAes_key(material.getAeskey());
        uploadResponse.setMd5(material.getMd5());
        uploadResponse.setSize(material.getFileSize());
        uploadResponse.setWidth(material.getWidth());
        uploadResponse.setHeight(material.getHeight());
        uploadResponse.setThumb_image_height(material.getThumbImageHeight());
        uploadResponse.setThumb_image_width(material.getThumbImageWidth());
        uploadResponse.setThumb_file_size(material.getThumbFileSize());
        uploadResponse.setThumb_file_md5(material.getThumbFileMd5());
        return validateFileUploadResponse(uploadResponse);
    }

    private String resolveCdnKey(CdnUploadResponse uploadResponse) {
        return StringUtils.hasText(uploadResponse.getCdn_key()) ? uploadResponse.getCdn_key() : uploadResponse.getFileid();
    }

    private String resolveMaterialCdnKey(MediaMaterial material) {
        return StringUtils.hasText(material.getCdnkey()) ? material.getCdnkey() : material.getFileId();
    }

    private String resolveFileName(MediaMaterial material) {
        return firstNonBlank(material.getFileName(), material.getFilename());
    }

    private String requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private Integer requireInteger(Integer value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private Integer resolveResponseCode(JSONObject result) {
        if (result.containsKey("errcode")) {
            return result.getInteger("errcode");
        }
        return result.getInteger("code");
    }

    private String resolveResponseMessage(JSONObject result) {
        return firstNonBlank(result.getString("errmsg"), result.getString("msg"));
    }

    private WxUserInfo resolveSenderUserInfo(Long senderUserId) {
        if (senderUserId == null) {
            return null;
        }
        WxUserInfo userInfo = wxUserInfoService.selectByVid(senderUserId);
        return userInfo != null ? userInfo : wxUserInfoService.selectByUserId(senderUserId);
    }

    private String resolveFriendName(WxFriendInfo friendInfo) {
        if (friendInfo == null) {
            return "unknown";
        }
        if (StringUtils.hasText(friendInfo.getRealname())) {
            return friendInfo.getRealname();
        }
        return firstNonBlank(friendInfo.getNickname(), "unknown");
    }

    private Long parseLongSafely(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String firstNonBlank(String first, String second) {
        return StringUtils.hasText(first) ? first : second;
    }

    private Integer firstNonNull(Integer first, Integer second) {
        return first != null ? first : second;
    }

    private interface MassTaskMessageSender {
        JSONObject send(MassTask task, ReceiverContext receiverContext);
    }

    private static class ReceiverContext {
        private final String senderUuid;
        private final Long receiverUserId;
        private final boolean roomMessage;
        private final String receiverName;

        private ReceiverContext(String senderUuid, Long receiverUserId, boolean roomMessage, String receiverName) {
            this.senderUuid = senderUuid;
            this.receiverUserId = receiverUserId;
            this.roomMessage = roomMessage;
            this.receiverName = receiverName;
        }

        public String getSenderUuid() {
            return senderUuid;
        }

        public Long getReceiverUserId() {
            return receiverUserId;
        }

        public boolean isRoomMessage() {
            return roomMessage;
        }

        public String getReceiverName() {
            return receiverName;
        }
    }

    private static class MediaMaterial {
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

        private static MediaMaterial fromPayload(MassTaskMediaPayload payload) {
            MediaMaterial material = new MediaMaterial();
            material.url = payload.getUrl();
            material.base64 = payload.getBase64();
            material.filename = payload.getFilename();
            material.contentType = payload.getContentType();
            material.voiceTime = payload.getVoiceTime();
            material.isHd = payload.getIsHd();
            return material;
        }

        private static MediaMaterial fromJson(JSONObject json) {
            MediaMaterial material = new MediaMaterial();
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

        private boolean hasSourcePayload() {
            return StringUtils.hasText(url) || StringUtils.hasText(base64);
        }

        private ReplyMediaItem toReplyMediaItem() {
            ReplyMediaItem item = new ReplyMediaItem();
            item.setUrl(url);
            item.setBase64(base64);
            item.setFilename(StringUtils.hasText(fileName) ? fileName : filename);
            item.setContentType(contentType);
            item.setIsHd(isHd);
            item.setVoiceTime(voiceTime);
            return item;
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

        private static String readText(JSONObject json, String... keys) {
            for (String key : keys) {
                String value = json.getString(key);
                if (StringUtils.hasText(value)) {
                    return value;
                }
            }
            return null;
        }

        private static Integer readInt(JSONObject json, String... keys) {
            for (String key : keys) {
                Integer value = json.getInteger(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }

    private static class AppMessageMaterial {
        private String desc;
        private String appName;
        private String title;
        private String weappIconUrl;
        private String pagepath;
        private String username;
        private String appid;
        private MediaMaterial coverMaterial;

        private static AppMessageMaterial fromPayload(MassTaskAppPayload payload) {
            AppMessageMaterial material = new AppMessageMaterial();
            material.desc = payload.getDesc();
            material.appName = payload.getAppName();
            material.title = payload.getTitle();
            material.weappIconUrl = payload.getWeappIconUrl();
            material.pagepath = payload.getPagepath();
            material.username = payload.getUsername();
            material.appid = payload.getAppid();
            if (payload.getCover() != null) {
                material.coverMaterial = MediaMaterial.fromPayload(payload.getCover());
            }
            return material;
        }

        private static AppMessageMaterial fromJson(JSONObject json) {
            AppMessageMaterial material = new AppMessageMaterial();
            material.desc = json.getString("desc");
            material.appName = MediaMaterial.readText(json, "appName", "app_name");
            material.title = json.getString("title");
            material.weappIconUrl = MediaMaterial.readText(json, "weappIconUrl", "weapp_icon_url");
            material.pagepath = json.getString("pagepath");
            material.username = json.getString("username");
            material.appid = json.getString("appid");
            JSONObject coverJson = json.getJSONObject("cover");
            if (coverJson != null) {
                material.coverMaterial = MediaMaterial.fromJson(coverJson);
            } else if (json.containsKey("cdnkey") || json.containsKey("cdn_key") || json.containsKey("fileid")) {
                material.coverMaterial = MediaMaterial.fromJson(json);
            }
            return material;
        }

        public String getDesc() { return desc; }
        public String getAppName() { return appName; }
        public String getTitle() { return title; }
        public String getWeappIconUrl() { return weappIconUrl; }
        public String getPagepath() { return pagepath; }
        public String getUsername() { return username; }
        public String getAppid() { return appid; }
        public MediaMaterial getCoverMaterial() { return coverMaterial; }
    }
}
