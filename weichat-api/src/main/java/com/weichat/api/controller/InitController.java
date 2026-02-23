package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/init")
public class InitController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/init")
    public ApiResult init(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/init", params));
    }

    @PostMapping("/setCallbackUrl")
    public ApiResult setCallbackUrl(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetCallbackUrl", params));
    }

    @PostMapping("/getRunClient")
    public ApiResult getRunClient(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetRunClient", params));
    }

    @PostMapping("/getRunClientByUuid")
    public ApiResult getRunClientByUuid(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetRunClientByUuid", params));
    }

    @PostMapping("/closeConnect")
    public ApiResult closeConnect(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CloseConnent", params));
    }

    @PostMapping("/setProxy")
    public ApiResult setProxy(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/setProxy", params));
    }

    @PostMapping("/getQrCode")
    public ApiResult getQrCode(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getQrCode", params));
    }

    @PostMapping("/checkCode")
    public ApiResult checkCode(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CheckCode", params));
    }

    @PostMapping("/automaticLogin")
    public ApiResult automaticLogin(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/automaticLogin", params));
    }

    @PostMapping("/loginOut")
    public ApiResult loginOut(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/LoginOut", params));
    }

    @PostMapping("/secondaryValidation")
    public ApiResult secondaryValidation(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SecondaryValidation", params));
    }
}
