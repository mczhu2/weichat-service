package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.message.*;
import com.weichat.api.vo.response.message.SendMsgResponse;
import com.weichat.api.vo.response.message.SyncDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息服务Controller
 *
 * @author weichat
 */
@Api(tags = "消息服务")
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("发送文本消息")
    @PostMapping("/sendText")
    public ApiResult<SendMsgResponse> sendText(@RequestBody SendTextRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送文本+表情消息")
    @PostMapping("/sendTextAndExp")
    public ApiResult<SendMsgResponse> sendTextAndExp(@RequestBody SendTextAndExpRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextAndExpMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送图片消息")
    @PostMapping("/sendImage")
    public ApiResult<SendMsgResponse> sendImage(@RequestBody SendImageRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNImgMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送文件消息")
    @PostMapping("/sendFile")
    public ApiResult<SendMsgResponse> sendFile(@RequestBody SendFileRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNFileMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送语音消息")
    @PostMapping("/sendVoice")
    public ApiResult<SendMsgResponse> sendVoice(@RequestBody SendVoiceRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNVoiceMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送视频消息")
    @PostMapping("/sendVideo")
    public ApiResult<SendMsgResponse> sendVideo(@RequestBody SendVideoRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNVideoMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送大视频消息")
    @PostMapping("/sendBigVideo")
    public ApiResult<SendMsgResponse> sendBigVideo(@RequestBody SendBigVideoRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNBigVideoMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送大文件消息")
    @PostMapping("/sendBigFile")
    public ApiResult<SendMsgResponse> sendBigFile(@RequestBody SendFileRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNBigFileMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送链接消息")
    @PostMapping("/sendLink")
    public ApiResult<SendMsgResponse> sendLink(@RequestBody SendLinkRequest request) {
        return ApiResult.from(client.post("/wxwork/SendLinkMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送小程序消息")
    @PostMapping("/sendApp")
    public ApiResult<SendMsgResponse> sendApp(@RequestBody SendAppRequest request) {
        return ApiResult.from(client.post("/wxwork/SendAppMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送视频号消息")
    @PostMapping("/sendVideoNumber")
    public ApiResult<SendMsgResponse> sendVideoNumber(@RequestBody SendVideoNumberRequest request) {
        return ApiResult.from(client.post("/wxwork/SendVideoNumber", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送视频号直播消息")
    @PostMapping("/sendVideoNumberLive")
    public ApiResult<SendMsgResponse> sendVideoNumberLive(@RequestBody SendVideoNumberLiveRequest request) {
        return ApiResult.from(client.post("/wxwork/SendVideoNumberZhiBo", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送表情消息")
    @PostMapping("/sendEmotion")
    public ApiResult<SendMsgResponse> sendEmotion(@RequestBody SendEmotionRequest request) {
        return ApiResult.from(client.post("/wxwork/SendEmotionMessage", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送名片消息")
    @PostMapping("/sendBusinessCard")
    public ApiResult<SendMsgResponse> sendBusinessCard(@RequestBody SendBusinessCardRequest request) {
        return ApiResult.from(client.post("/wxwork/SendBusinessCardMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送位置消息")
    @PostMapping("/sendLocation")
    public ApiResult<SendMsgResponse> sendLocation(@RequestBody SendLocationRequest request) {
        return ApiResult.from(client.post("/wxwork/SendLocationMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送@文本消息")
    @PostMapping("/sendTextAt")
    public ApiResult<SendMsgResponse> sendTextAt(@RequestBody SendTextAtRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextAtMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送@文本消息(高级)")
    @PostMapping("/sendTextAtTwo")
    public ApiResult<SendMsgResponse> sendTextAtTwo(@RequestBody SendTextAtTwoRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextAtMsgTwo", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("发送多群消息")
    @PostMapping("/sendGroupsMsg")
    public ApiResult<SendMsgResponse> sendGroupsMsg(@RequestBody SendGroupsMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/SendGroupsMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("撤回消息")
    @PostMapping("/revokeMsg")
    public ApiResult<Void> revokeMsg(@RequestBody RevokeMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/RevokeMsg", toJson(request)));
    }

    @ApiOperation("同步所有数据")
    @PostMapping("/syncAllData")
    public ApiResult<SyncDataResponse> syncAllData(@RequestBody SyncAllDataRequest request) {
        return ApiResult.from(client.post("/wxwork/SyncAllData", toJson(request)), SyncDataResponse.class);
    }

    @ApiOperation("语音转文字")
    @PostMapping("/speechToText")
    public ApiResult<Void> speechToText(@RequestBody SpeechToTextRequest request) {
        return ApiResult.from(client.post("/wxwork/SpeechToTextEntity", toJson(request)));
    }

    @ApiOperation("标记已读")
    @PostMapping("/markAsRead")
    public ApiResult<Void> markAsRead(@RequestBody MarkAsReadRequest request) {
        return ApiResult.from(client.post("/wxwork/MarkAsRead", toJson(request)));
    }

    @ApiOperation("发送引用消息")
    @PostMapping("/sendQuoteMsg")
    public ApiResult<SendMsgResponse> sendQuoteMsg(@RequestBody SendQuoteMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/sendQuoteMsg", toJson(request)), SendMsgResponse.class);
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
