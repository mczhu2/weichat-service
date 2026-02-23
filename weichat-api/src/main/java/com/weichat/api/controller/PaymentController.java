package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/merchantInfo")
    public ApiResult merchantInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/MerchantInformation", params));
    }

    @PostMapping("/getMerchantQrCode")
    public ApiResult getMerchantQrCode(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetUserMerchantQrCode", params));
    }

    @PostMapping("/sendPayment")
    public ApiResult sendPayment(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/sendPayment", params));
    }
}
