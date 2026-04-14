package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.CdnFileService;
import com.weichat.api.vo.request.message.SendVideoRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Autowired
    private CdnFileService cdnFileService;

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.VIDEO.getCode();
    }

    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        List<MassTaskMediaMaterial> materials = messageSupport.resolveMediaMaterials(
                task,
                task.getVideoMediaId(),
                receiverContext.getReceiverName(),
                "video material is empty"
        );

        String textContent = messageSupport.resolveStructuredText(task, receiverContext.getReceiverName());
        if (org.springframework.util.StringUtils.hasText(textContent)) {
            messageSupport.ensureSuccess(
                    messageSupport.sendTextMessage(receiverContext, textContent),
                    "send video text"
            );
        }

        for (MassTaskMediaMaterial material : materials) {
            SendVideoRequest request;
            if (material.hasSourcePayload()) {
                CdnUploadResponse uploadResponse = messageSupport.validateFileUploadResponse(
                        cdnFileService.uploadVideoFile(receiverContext.getSenderUuid(), material.toReplyMediaItem())
                );
                request = SendVideoRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.resolveCdnKey(uploadResponse))
                        .aeskey(uploadResponse.getAes_key())
                        .md5(uploadResponse.getMd5())
                        .video_duration(uploadResponse.getVideoDuration())
                        .video_width(messageSupport.firstNonNull(material.getVideoWidth(), uploadResponse.getWidth()))
                        .video_height(messageSupport.firstNonNull(material.getVideoHeight(), uploadResponse.getHeight()))
                        .fileSize(uploadResponse.getSize())
                        .build();
            } else {
                request = SendVideoRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.requireText(messageSupport.resolveMaterialCdnKey(material), "video cdnkey is required"))
                        .aeskey(messageSupport.requireText(material.getAeskey(), "video aeskey is required"))
                        .md5(messageSupport.requireText(material.getMd5(), "video md5 is required"))
                        .video_duration(0)
                        .video_width(material.getVideoWidth())
                        .video_height(material.getVideoHeight())
                        .fileSize(messageSupport.requireInteger(material.getFileSize(), "video fileSize is required"))
                        .build();
            }

            messageSupport.ensureSuccess(
                    messageSupport.postMessage("/wxwork/SendCDNVideoMsg", request),
                    "send video"
            );
        }
        return messageSupport.successResult();
    }
}
