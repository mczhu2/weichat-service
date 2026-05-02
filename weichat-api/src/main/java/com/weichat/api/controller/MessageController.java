package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.CustomerReplyService;
import com.weichat.api.service.MessageSendService;
import com.weichat.api.vo.request.message.*;
import com.weichat.api.vo.response.message.SendMsgResponse;
import com.weichat.api.vo.response.message.SyncDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Message controller.
 */
@Api(tags = "Message")
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private WxWorkApiClient client;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private CustomerReplyService customerReplyService;

    @ApiOperation("Send text message")
    @PostMapping("/sendText")
    public ApiResult<SendMsgResponse> sendText(@RequestBody SendTextRequest request) {
        return messageSendService.sendText(request);
    }

    @ApiOperation("Send text and emotion message")
    @PostMapping("/sendTextAndExp")
    public ApiResult<SendMsgResponse> sendTextAndExp(@RequestBody SendTextAndExpRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextAndExpMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send image message")
    @PostMapping("/sendImage")
    public ApiResult<SendMsgResponse> sendImage(@RequestBody SendImageRequest request) {
        return messageSendService.sendImage(request);
    }

    @ApiOperation("Send file message")
    @PostMapping("/sendFile")
    public ApiResult<SendMsgResponse> sendFile(@RequestBody SendFileRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNFileMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send voice message")
    @PostMapping("/sendVoice")
    public ApiResult<SendMsgResponse> sendVoice(@RequestBody SendVoiceRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNVoiceMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send friend reply message")
    @PostMapping("/sendFriendReply")
    public ApiResult<Void> sendFriendReply(@RequestBody SendFriendReplyRequest request) {
        return customerReplyService.sendFriendReply(request);
    }

    @ApiOperation("Handle wecom agent async reply callback")
    @PostMapping("/wecomAgentReplyCallback")
    public ApiResult<Void> wecomAgentReplyCallback(@RequestBody WecomAgentReplyCallbackRequest request) {
        return customerReplyService.handleWecomAgentReplyCallback(request);
    }

    @ApiOperation("Send video message")
    @PostMapping("/sendVideo")
    public ApiResult<SendMsgResponse> sendVideo(@RequestBody SendVideoRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNVideoMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send big video message")
    @PostMapping("/sendBigVideo")
    public ApiResult<SendMsgResponse> sendBigVideo(@RequestBody SendBigVideoRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNBigVideoMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send big file message")
    @PostMapping("/sendBigFile")
    public ApiResult<SendMsgResponse> sendBigFile(@RequestBody SendFileRequest request) {
        return ApiResult.from(client.post("/wxwork/SendCDNBigFileMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send link message")
    @PostMapping("/sendLink")
    public ApiResult<SendMsgResponse> sendLink(@RequestBody SendLinkRequest request) {
        return ApiResult.from(client.post("/wxwork/SendLinkMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send mini app message")
    @PostMapping("/sendApp")
    public ApiResult<SendMsgResponse> sendApp(@RequestBody SendAppMessageRequest request) {
        return messageSendService.sendApp(request);
    }

    @ApiOperation("Send video number message")
    @PostMapping("/sendVideoNumber")
    public ApiResult<SendMsgResponse> sendVideoNumber(@RequestBody SendVideoNumberRequest request) {
        return ApiResult.from(client.post("/wxwork/SendVideoNumber", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send video number live message")
    @PostMapping("/sendVideoNumberLive")
    public ApiResult<SendMsgResponse> sendVideoNumberLive(@RequestBody SendVideoNumberLiveRequest request) {
        return ApiResult.from(client.post("/wxwork/SendVideoNumberZhiBo", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send emotion message")
    @PostMapping("/sendEmotion")
    public ApiResult<SendMsgResponse> sendEmotion(@RequestBody SendEmotionRequest request) {
        return ApiResult.from(client.post("/wxwork/SendEmotionMessage", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send business card message")
    @PostMapping("/sendBusinessCard")
    public ApiResult<SendMsgResponse> sendBusinessCard(@RequestBody SendBusinessCardRequest request) {
        return ApiResult.from(client.post("/wxwork/SendBusinessCardMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send location message")
    @PostMapping("/sendLocation")
    public ApiResult<SendMsgResponse> sendLocation(@RequestBody SendLocationRequest request) {
        return ApiResult.from(client.post("/wxwork/SendLocationMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send text at message")
    @PostMapping("/sendTextAt")
    public ApiResult<SendMsgResponse> sendTextAt(@RequestBody SendTextAtRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextAtMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send advanced text at message")
    @PostMapping("/sendTextAtTwo")
    public ApiResult<SendMsgResponse> sendTextAtTwo(@RequestBody SendTextAtTwoRequest request) {
        return ApiResult.from(client.post("/wxwork/SendTextAtMsgTwo", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Send group messages")
    @PostMapping("/sendGroupsMsg")
    public ApiResult<SendMsgResponse> sendGroupsMsg(@RequestBody SendGroupsMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/SendGroupsMsg", toJson(request)), SendMsgResponse.class);
    }

    @ApiOperation("Revoke message")
    @PostMapping("/revokeMsg")
    public ApiResult<Void> revokeMsg(@RequestBody RevokeMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/RevokeMsg", toJson(request)));
    }

    @ApiOperation("Sync all data")
    @PostMapping("/syncAllData")
    public ApiResult<SyncDataResponse> syncAllData(@RequestBody SyncAllDataRequest request) {
        return ApiResult.from(client.post("/wxwork/SyncAllData", toJson(request)), SyncDataResponse.class);
    }

    @ApiOperation("Speech to text")
    @PostMapping("/speechToText")
    public ApiResult<Void> speechToText(@RequestBody SpeechToTextRequest request) {
        return ApiResult.from(client.post("/wxwork/SpeechToTextEntity", toJson(request)));
    }

    @ApiOperation("Mark as read")
    @PostMapping("/markAsRead")
    public ApiResult<Void> markAsRead(@RequestBody MarkAsReadRequest request) {
        return ApiResult.from(client.post("/wxwork/MarkAsRead", toJson(request)));
    }

    @ApiOperation("Send quote message")
    @PostMapping("/sendQuoteMsg")
    public ApiResult<SendMsgResponse> sendQuoteMsg(@RequestBody SendQuoteMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/sendQuoteMsg", toJson(request)), SendMsgResponse.class);
    }

    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
