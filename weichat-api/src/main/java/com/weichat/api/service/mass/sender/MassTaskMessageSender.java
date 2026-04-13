package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.common.entity.MassTask;

public interface MassTaskMessageSender {

    Integer getMsgType();

    JSONObject send(MassTask task, MassTaskReceiverContext receiverContext);
}
