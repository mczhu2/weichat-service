package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getLabelList")
    public ApiResult getLabelList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetLabelListReq", params));
    }

    @PostMapping("/addLabel")
    public ApiResult addLabel(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddLabelReq", params));
    }

    @PostMapping("/editLabel")
    public ApiResult editLabel(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/EditLabelReq", params));
    }

    @PostMapping("/delLabel")
    public ApiResult delLabel(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DelLabelReq", params));
    }

    @PostMapping("/labelAddUser")
    public ApiResult labelAddUser(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/LabelAddUserReq", params));
    }

    @PostMapping("/userAddLabels")
    public ApiResult userAddLabels(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/UserAddLabelsReq", params));
    }
}
