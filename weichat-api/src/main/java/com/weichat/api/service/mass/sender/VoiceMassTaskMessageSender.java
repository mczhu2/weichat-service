package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.CdnFileService;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Component
public class VoiceMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Autowired
    private CdnFileService cdnFileService;

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.AUDIO.getCode();
    }

    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        Long taskId = task.getId();
        String taskName = task.getTaskName();
        String senderUuid = receiverContext.getSenderUuid();
        Long receiverUserId = receiverContext.getReceiverUserId();
        String receiverName = receiverContext.getReceiverName();
        boolean roomMessage = receiverContext.isRoomMessage();

        log.info("voice mass send start, taskId={}, taskName={}, senderUuid={}, receiverUserId={}, receiverName={}, isRoom={}",
                taskId, taskName, senderUuid, receiverUserId, receiverName, roomMessage);

        List<MassTaskMediaMaterial> materials;
        try {
            materials = messageSupport.resolveMediaMaterials(
                    task,
                    task.getAudioMediaId(),
                    receiverName,
                    "voice material is empty"
            );
        } catch (RuntimeException ex) {
            log.warn("voice mass send skip, taskId={}, receiverName={}, reason=resolve materials failed: {}",
                    taskId, receiverName, ex.getMessage());
            throw ex;
        }
        log.info("voice mass send materials resolved, taskId={}, receiverName={}, materialCount={}",
                taskId, receiverName, materials.size());

        String textContent = messageSupport.resolveStructuredText(task, receiverName);
        if (StringUtils.hasText(textContent)) {
            log.info("voice mass send accompanying text, taskId={}, receiverName={}, textLength={}",
                    taskId, receiverName, textContent.length());
            try {
                messageSupport.ensureSuccess(
                        messageSupport.sendTextMessage(receiverContext, textContent),
                        "send voice text"
                );
            } catch (RuntimeException ex) {
                log.error("voice mass send text failed, taskId={}, receiverName={}, reason={}",
                        taskId, receiverName, ex.getMessage());
                throw ex;
            }
        } else {
            log.info("voice mass send skip accompanying text, taskId={}, receiverName={}, reason=textContent is blank",
                    taskId, receiverName);
        }

        for (int i = 0; i < materials.size(); i++) {
            MassTaskMediaMaterial material = materials.get(i);
            SendVoiceRequest request;
            String cdnKey;
            Integer fileSize;
            Integer voiceTime;

            if (material.hasSourcePayload()) {
                log.info("voice mass send upload new material, taskId={}, receiverName={}, index={}, fileName={}, contentType={}",
                        taskId, receiverName, i, messageSupport.resolveFileName(material), material.getContentType());
                CdnUploadResponse uploadResponse;
                try {
                    uploadResponse = messageSupport.validateFileUploadResponse(
                            cdnFileService.uploadFile(senderUuid, material.toReplyMediaItem())
                    );
                } catch (RuntimeException ex) {
                    log.error("voice mass send upload failed, taskId={}, receiverName={}, index={}, reason={}",
                            taskId, receiverName, i, ex.getMessage());
                    throw ex;
                }
                cdnKey = messageSupport.resolveCdnKey(uploadResponse);
                fileSize = uploadResponse.getSize();
                voiceTime = resolveVoiceTime(material, fileSize);
                log.info("voice mass send upload success, taskId={}, receiverName={}, index={}, cdnKey={}, fileSize={}, voiceTime={}",
                        taskId, receiverName, i, cdnKey, fileSize, voiceTime);
                request = SendVoiceRequest.builder()
                        .uuid(senderUuid)
                        .send_userid(receiverUserId)
                        .isRoom(roomMessage)
                        .cdnkey(cdnKey)
                        .aeskey(uploadResponse.getAes_key())
                        .md5(uploadResponse.getMd5())
                        .voice_time(voiceTime)
                        .fileSize(fileSize)
                        .build();
            } else {
                cdnKey = messageSupport.requireText(messageSupport.resolveMaterialCdnKey(material), "voice cdnkey is required");
                fileSize = messageSupport.requireInteger(material.getFileSize(), "voice fileSize is required");
                voiceTime = resolveVoiceTime(material, fileSize);
                log.info("voice mass send reuse existing material, taskId={}, receiverName={}, index={}, cdnKey={}, fileSize={}, voiceTime={}",
                        taskId, receiverName, i, cdnKey, fileSize, voiceTime);
                request = SendVoiceRequest.builder()
                        .uuid(senderUuid)
                        .send_userid(receiverUserId)
                        .isRoom(roomMessage)
                        .cdnkey(cdnKey)
                        .aeskey(messageSupport.requireText(material.getAeskey(), "voice aeskey is required"))
                        .md5(messageSupport.requireText(material.getMd5(), "voice md5 is required"))
                        .voice_time(voiceTime)
                        .fileSize(fileSize)
                        .build();
            }

            try {
                messageSupport.ensureSuccess(
                        messageSupport.postMessage("/wxwork/SendCDNVoiceMsg", request),
                        "send voice"
                );
                log.info("voice mass send success, taskId={}, receiverName={}, index={}, cdnKey={}",
                        taskId, receiverName, i, cdnKey);
            } catch (RuntimeException ex) {
                log.error("voice mass send failed, taskId={}, receiverName={}, index={}, cdnKey={}, reason={}",
                        taskId, receiverName, i, cdnKey, ex.getMessage());
                throw ex;
            }
        }

        log.info("voice mass send finished, taskId={}, receiverName={}, materialCount={}",
                taskId, receiverName, materials.size());
        return messageSupport.successResult();
    }

    /**
     * 取素材自带 voice_time；缺失时按文件大小估算，最终兜底为 1。
     */
    private Integer resolveVoiceTime(MassTaskMediaMaterial material, Integer fileSize) {
        Integer voiceTime = material.getVoiceTime();
        if (voiceTime != null && voiceTime > 0) {
            return voiceTime;
        }
        Integer estimated = messageSupport.estimateVoiceTime(fileSize);
        return estimated != null ? estimated : 1;
    }
}
