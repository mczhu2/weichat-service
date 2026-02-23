package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/sendText")
    public ApiResult sendText(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendTextMsg", params));
    }

    @PostMapping("/sendTextAndExp")
    public ApiResult sendTextAndExp(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendTextAndExpMsg", params));
    }

    @PostMapping("/sendImage")
    public ApiResult sendImage(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendCDNImgMsg", params));
    }

    @PostMapping("/sendFile")
    public ApiResult sendFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendCDNFileMsg", params));
    }

    @PostMapping("/sendVoice")
    public ApiResult sendVoice(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendCDNVoiceMsg", params));
    }

    @PostMapping("/sendVideo")
    public ApiResult sendVideo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendCDNVideoMsg", params));
    }

    @PostMapping("/sendBigVideo")
    public ApiResult sendBigVideo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendCDNBigVideoMsg", params));
    }

    @PostMapping("/sendBigFile")
    public ApiResult sendBigFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendCDNBigFileMsg", params));
    }

    @PostMapping("/sendLink")
    public ApiResult sendLink(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendLinkMsg", params));
    }

    @PostMapping("/sendApp")
    public ApiResult sendApp(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendAppMsg", params));
    }

    @PostMapping("/sendVideoNumber")
    public ApiResult sendVideoNumber(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendVideoNumber", params));
    }

    @PostMapping("/sendVideoNumberLive")
    public ApiResult sendVideoNumberLive(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendVideoNumberZhiBo", params));
    }

    @PostMapping("/sendEmotion")
    public ApiResult sendEmotion(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendEmotionMessage", params));
    }

    @PostMapping("/sendBusinessCard")
    public ApiResult sendBusinessCard(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendBusinessCardMsg", params));
    }

    @PostMapping("/sendLocation")
    public ApiResult sendLocation(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendLocationMsg", params));
    }

    @PostMapping("/sendTextAt")
    public ApiResult sendTextAt(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendTextAtMsg", params));
    }

    @PostMapping("/sendTextAtTwo")
    public ApiResult sendTextAtTwo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendTextAtMsgTwo", params));
    }

    @PostMapping("/sendGroupsMsg")
    public ApiResult sendGroupsMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendGroupsMsg", params));
    }

    @PostMapping("/revokeMsg")
    public ApiResult revokeMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/RevokeMsg", params));
    }

    @PostMapping("/syncAllData")
    public ApiResult syncAllData(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SyncAllData", params));
    }

    @PostMapping("/speechToText")
    public ApiResult speechToText(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SpeechToTextEntity", params));
    }

    @PostMapping("/markAsRead")
    public ApiResult markAsRead(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/MarkAsRead", params));
    }

    @PostMapping("/sendQuoteMsg")
    public ApiResult sendQuoteMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/sendQuoteMsg", params));
    }
}
