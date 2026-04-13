package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.CdnFileService;
import com.weichat.api.vo.request.message.SendFileRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Autowired
    private CdnFileService cdnFileService;

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.FILE.getCode();
    }

    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        List<MassTaskMediaMaterial> materials = messageSupport.resolveMediaMaterials(
                task,
                task.getFileMediaId(),
                receiverContext.getReceiverName(),
                "file material is empty"
        );

        String textContent = messageSupport.resolveStructuredText(task, receiverContext.getReceiverName());
        if (org.springframework.util.StringUtils.hasText(textContent)) {
            messageSupport.ensureSuccess(
                    messageSupport.sendTextMessage(receiverContext, textContent),
                    "send file text"
            );
        }

        for (MassTaskMediaMaterial material : materials) {
            SendFileRequest request;
            if (material.hasSourcePayload()) {
                CdnUploadResponse uploadResponse = messageSupport.validateFileUploadResponse(
                        cdnFileService.uploadFile(receiverContext.getSenderUuid(), material.toReplyMediaItem())
                );
                request = SendFileRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.resolveCdnKey(uploadResponse))
                        .aeskey(uploadResponse.getAes_key())
                        .md5(uploadResponse.getMd5())
                        .file_name(messageSupport.resolveFileName(material))
                        .fileSize(uploadResponse.getSize())
                        .build();
            } else {
                request = SendFileRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.requireText(messageSupport.resolveMaterialCdnKey(material), "file cdnkey is required"))
                        .aeskey(messageSupport.requireText(material.getAeskey(), "file aeskey is required"))
                        .md5(messageSupport.requireText(material.getMd5(), "file md5 is required"))
                        .file_name(messageSupport.requireText(messageSupport.resolveFileName(material), "file name is required"))
                        .fileSize(messageSupport.requireInteger(material.getFileSize(), "fileSize is required"))
                        .build();
            }

            messageSupport.ensureSuccess(
                    messageSupport.postMessage("/wxwork/SendCDNFileMsg", request),
                    "send file"
            );
        }
        return messageSupport.successResult();
    }
}
