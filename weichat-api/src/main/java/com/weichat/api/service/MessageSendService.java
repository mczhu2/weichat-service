package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.message.SendImageRequest;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.response.message.SendMsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSendService {

    @Autowired
    private WxWorkApiClient client;

    @Autowired
    private AsyncMessagePersistenceService asyncMessagePersistenceService;

    public ApiResult<SendMsgResponse> sendText(SendTextRequest request) {
        JSONObject response = client.post("/wxwork/SendTextMsg", JSON.parseObject(JSON.toJSONString(request)));
        asyncMessagePersistenceService.saveSentTextMessage(request, response);
        return ApiResult.from(response, SendMsgResponse.class);
    }

    /**
     * 发送 CDN 图片消息，调用方需提前完成图片上传并填充完整媒体信息。
     */
    public ApiResult<SendMsgResponse> sendImage(SendImageRequest request) {
        JSONObject response = client.post("/wxwork/SendCDNImgMsg", JSON.parseObject(JSON.toJSONString(request)));
        return ApiResult.from(response, SendMsgResponse.class);
    }
}
