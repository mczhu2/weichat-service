package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.payment.*;
import com.weichat.api.vo.response.init.QrCodeResponse;
import com.weichat.api.vo.response.payment.MerchantInfoResponse;
import com.weichat.api.vo.response.payment.PaymentResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付功能Controller
 *
 * @author weichat
 */
@Api(tags = "支付功能")
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取商户信息")
    @PostMapping("/merchantInfo")
    public ApiResult<MerchantInfoResponse> merchantInfo(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/MerchantInformation", toJson(request)), MerchantInfoResponse.class);
    }

    @ApiOperation("获取商户二维码")
    @PostMapping("/getMerchantQrCode")
    public ApiResult<QrCodeResponse> getMerchantQrCode(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetUserMerchantQrCode", toJson(request)), QrCodeResponse.class);
    }

    @ApiOperation("发送支付")
    @PostMapping("/sendPayment")
    public ApiResult<PaymentResultResponse> sendPayment(@RequestBody SendPaymentRequest request) {
        return ApiResult.from(client.post("/wxwork/sendPayment", toJson(request)), PaymentResultResponse.class);
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
