package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.CdnImageService;
import com.weichat.api.vo.request.message.SendImageRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Autowired
    private CdnImageService cdnImageService;

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.IMAGE.getCode();
    }

    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        List<MassTaskMediaMaterial> materials = messageSupport.resolveMediaMaterials(
                task,
                task.getImageMediaId(),
                receiverContext.getReceiverName(),
                "image material is empty"
        );

        String textContent = messageSupport.resolveStructuredText(task, receiverContext.getReceiverName());
        if (org.springframework.util.StringUtils.hasText(textContent)) {
            messageSupport.ensureSuccess(
                    messageSupport.sendTextMessage(receiverContext, textContent),
                    "send image text"
            );
        }

        for (MassTaskMediaMaterial material : materials) {
            SendImageRequest request;
            if (material.hasSourcePayload()) {
                CdnUploadResponse uploadResponse = messageSupport.validateImageUploadResponse(
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
                        .width(messageSupport.firstNonNull(material.getWidth(), uploadResponse.getWidth()))
                        .height(messageSupport.firstNonNull(material.getHeight(), uploadResponse.getHeight()))
                        .thumb_image_height(messageSupport.firstNonNull(material.getThumbImageHeight(), uploadResponse.getThumb_image_height()))
                        .thumb_image_width(messageSupport.firstNonNull(material.getThumbImageWidth(), uploadResponse.getThumb_image_width()))
                        .thumb_file_size(messageSupport.firstNonNull(material.getThumbFileSize(), uploadResponse.getThumb_file_size()))
                        .thumb_file_md5(messageSupport.firstNonBlank(material.getThumbFileMd5(), uploadResponse.getThumb_file_md5()))
                        .is_hd(material.getIsHd())
                        .build();
            } else {
                request = SendImageRequest.builder()
                        .uuid(receiverContext.getSenderUuid())
                        .send_userid(receiverContext.getReceiverUserId())
                        .isRoom(receiverContext.isRoomMessage())
                        .cdnkey(messageSupport.requireText(material.getCdnkey(), "image cdnkey is required"))
                        .aeskey(messageSupport.requireText(material.getAeskey(), "image aeskey is required"))
                        .md5(messageSupport.requireText(material.getMd5(), "image md5 is required"))
                        .fileSize(messageSupport.requireInteger(material.getFileSize(), "image fileSize is required"))
                        .width(material.getWidth())
                        .height(material.getHeight())
                        .thumb_image_height(material.getThumbImageHeight())
                        .thumb_image_width(material.getThumbImageWidth())
                        .thumb_file_size(material.getThumbFileSize())
                        .thumb_file_md5(material.getThumbFileMd5())
                        .is_hd(material.getIsHd())
                        .build();
            }

            messageSupport.ensureSuccess(
                    messageSupport.postMessage("/wxwork/SendCDNImgMsg", request),
                    "send image"
            );
        }
        return messageSupport.successResult();
    }
}
