package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getProfile")
    public ApiResult getProfile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetProfile", params));
    }

    @PostMapping("/getThisQrCode")
    public ApiResult getThisQrCode(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetThisQrCode", params));
    }

    @PostMapping("/externalBusinessCard")
    public ApiResult externalBusinessCard(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/ExternalBusinessCard", params));
    }

    @PostMapping("/addImage")
    public ApiResult addImage(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddImage", params));
    }

    @PostMapping("/updateUserInfo")
    public ApiResult updateUserInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/UpdateUserInfo", params));
    }

    @PostMapping("/automaticConsent")
    public ApiResult automaticConsent(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AutomaticConsent", params));
    }

    @PostMapping("/getCorpInfo")
    public ApiResult getCorpInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetCorpInfo", params));
    }

    @PostMapping("/getBanDetail")
    public ApiResult getBanDetail(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getBanDetile", params));
    }

    @PostMapping("/uploadImage")
    public ApiResult uploadImage(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/add_image", params));
    }
}
