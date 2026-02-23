package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chattag")
public class ChatTagController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/modSessionTag")
    public ApiResult modSessionTag(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/ModSessionTag", params));
    }

    @PostMapping("/getSessionTagList")
    public ApiResult getSessionTagList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetSessionTagList", params));
    }

    @PostMapping("/getSessionInTag")
    public ApiResult getSessionInTag(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetSessionInTagReq", params));
    }

    @PostMapping("/sessionInTag")
    public ApiResult sessionInTag(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SessionInTagReq", params));
    }
}
