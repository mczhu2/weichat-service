package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.vo.request.mass.MassTaskLinkPayload;
import com.weichat.api.vo.request.message.SendLinkRequest;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.LINK.getCode();
    }

    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        MassTaskLinkPayload payload = messageSupport.resolveLinkPayload(task, receiverContext.getReceiverName());
        SendLinkRequest request = SendLinkRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .url(messageSupport.requireText(payload.getUrl(), "link url is required"))
                .title(messageSupport.requireText(payload.getTitle(), "link title is required"))
                .content(messageSupport.requireText(payload.getContent(), "link content is required"))
                .imgurl(payload.getImgurl())
                .build();
        return messageSupport.postMessage("/wxwork/SendLinkMsg", request);
    }
}
