package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/moment")
public class MomentController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getSnsList")
    public ApiResult getSnsList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetSnsList", params));
    }

    @PostMapping("/getSnsInfo")
    public ApiResult getSnsInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetSnsInfo", params));
    }

    @PostMapping("/postPyq")
    public ApiResult postPyq(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/PostPyq", params));
    }

    @PostMapping("/deleteSns")
    public ApiResult deleteSns(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DeleteSns", params));
    }

    @PostMapping("/comment")
    public ApiResult comment(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CommentSnsReq", params));
    }

    @PostMapping("/deleteComment")
    public ApiResult deleteComment(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DeleteSnsComment", params));
    }

    @PostMapping("/likeOrUnlike")
    public ApiResult likeOrUnlike(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/LikeOrUnLikeSns", params));
    }
}
