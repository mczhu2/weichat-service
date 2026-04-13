package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.service.CdnFileService;
import com.weichat.api.vo.request.message.SendAppRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Autowired
    private CdnFileService cdnFileService;

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.APP.getCode();
    }

    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        MassTaskAppMessageMaterial appMaterial = messageSupport.resolveAppMessageMaterial(task, receiverContext.getReceiverName());
        MassTaskMediaMaterial coverMaterial = appMaterial.getCoverMaterial();
        if (coverMaterial == null) {
            throw new IllegalArgumentException("app cover is required");
        }

        CdnUploadResponse uploadResponse = coverMaterial.hasSourcePayload()
                ? messageSupport.validateFileUploadResponse(
                cdnFileService.uploadFile(receiverContext.getSenderUuid(), coverMaterial.toReplyMediaItem()))
                : messageSupport.buildUploadResponseFromMaterial(coverMaterial);

        SendAppRequest request = SendAppRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .title(messageSupport.requireText(appMaterial.getTitle(), "app title is required"))
                .desc(messageSupport.requireText(appMaterial.getDesc(), "app desc is required"))
                .appName(messageSupport.requireText(appMaterial.getAppName(), "appName is required"))
                .appid(messageSupport.requireText(appMaterial.getAppid(), "appid is required"))
                .username(messageSupport.requireText(appMaterial.getUsername(), "username is required"))
                .pagepath(messageSupport.requireText(appMaterial.getPagepath(), "pagepath is required"))
                .weappIconUrl(appMaterial.getWeappIconUrl())
                .cdnkey(messageSupport.requireText(messageSupport.resolveCdnKey(uploadResponse), "app cover cdnkey is required"))
                .aeskey(messageSupport.requireText(uploadResponse.getAes_key(), "app cover aeskey is required"))
                .md5(messageSupport.requireText(uploadResponse.getMd5(), "app cover md5 is required"))
                .fileSize(messageSupport.requireInteger(uploadResponse.getSize(), "app cover fileSize is required"))
                .build();
        return messageSupport.postMessage("/wxwork/SendAppMsg", request);
    }
}
