package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service")
public class ServiceController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getKFList")
    public ApiResult getKFList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getKFList", params));
    }

    @PostMapping("/getKFInfo")
    public ApiResult getKFInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getKFInfo", params));
    }

    @PostMapping("/getKFState")
    public ApiResult getKFState(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getKFState", params));
    }

    @PostMapping("/setKFState")
    public ApiResult setKFState(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/setKFState", params));
    }

    @PostMapping("/getWaitQueue")
    public ApiResult getWaitQueue(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getWxKfWaitQueueItems", params));
    }

    @PostMapping("/connectWithCustomers")
    public ApiResult connectWithCustomers(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/connectWithCustomers", params));
    }

    @PostMapping("/getTransferList")
    public ApiResult getTransferList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getWxKfSingleCanTransferList", params));
    }

    @PostMapping("/endChat")
    public ApiResult endChat(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/kfEndChat", params));
    }

    @PostMapping("/switchChat")
    public ApiResult switchChat(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/kfSwitchChat", params));
    }
}
