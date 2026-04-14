package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.CdnFileService;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        List<MassTaskMediaMaterial> materials = messageSupport.resolveMediaMaterials(
                task,
                task.getAudioMediaId(),
                receiverContext.getReceiverName(),
                "voice material is empty"
        );

        String textContent = messageSupport.resolveStructuredText(task, receiverContext.getReceiverName());
        if (org.springframework.util.StringUtils.hasText(textContent)) {
            messageSupport.ensureSuccess(
                    messageSupport.sendTextMessage(receiverContext, textContent),
                    "send voice text"
            );
        }

        for (MassTaskMediaMaterial material : materials) {
            SendVoiceRequest request;
            if (material.hasSourcePayload()) {
                CdnUploadResponse uploadResponse = messageSupport.validateFileUploadResponse(
                        cdnFileService.uploadVideoFile(receiverContext.getSenderUuid(), material.toReplyMediaItem())
                );
                request = SendVoiceRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.resolveCdnKey(uploadResponse))
                        .aeskey(uploadResponse.getAes_key())
                        .md5(uploadResponse.getMd5())
                        .voice_time(messageSupport.resolveVoiceDuration(uploadResponse))
                        .fileSize(uploadResponse.getSize())
                        .build();
            } else {
                request = SendVoiceRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.requireText(messageSupport.resolveMaterialCdnKey(material), "voice cdnkey is required"))
                        .aeskey(messageSupport.requireText(material.getAeskey(), "voice aeskey is required"))
                        .md5(messageSupport.requireText(material.getMd5(), "voice md5 is required"))
                        .voice_time(0)
                        .fileSize(messageSupport.requireInteger(material.getFileSize(), "voice fileSize is required"))
                        .build();
            }

            messageSupport.ensureSuccess(
                    messageSupport.postMessage("/wxwork/SendCDNVoiceMsg", request),
                    "send voice"
            );
        }
        return messageSupport.successResult();
    }
}
