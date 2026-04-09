package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.account.*;
import com.weichat.api.vo.response.account.CorpInfoResponse;
import com.weichat.api.vo.response.account.ProfileResponse;
import com.weichat.api.vo.response.init.QrCodeResponse;
import com.weichat.api.vo.response.init.ThisQrCodeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账号管理Controller
 *
 * @author weichat
 */
@Api(tags = "账号管理")
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取账号信息")
    @PostMapping("/getProfile")
    public ApiResult<ProfileResponse> getProfile(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetProfile", toJson(request)), ProfileResponse.class);
    }

    @ApiOperation("获取当前二维码")
    @PostMapping("/getThisQrCode")
    public ApiResult<ThisQrCodeResponse> getThisQrCode(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetThisQrCode", toJson(request)), ThisQrCodeResponse.class);
    }

    @ApiOperation("获取外部名片")
    @PostMapping("/externalBusinessCard")
    public ApiResult<ProfileResponse> externalBusinessCard(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/ExternalBusinessCard", toJson(request)), ProfileResponse.class);
    }

    @ApiOperation("添加图片")
    @PostMapping("/addImage")
    public ApiResult<Void> addImage(@RequestBody AddImageRequest request) {
        return ApiResult.from(client.post("/wxwork/AddImage", toJson(request)));
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/updateUserInfo")
    public ApiResult<Void> updateUserInfo(@RequestBody UpdateUserInfoRequest request) {
        return ApiResult.from(client.post("/wxwork/UpdateUserInfo", toJson(request)));
    }

    @ApiOperation("自动同意设置")
    @PostMapping("/automaticConsent")
    public ApiResult<Void> automaticConsent(@RequestBody AutomaticConsentRequest request) {
        return ApiResult.from(client.post("/wxwork/AutomaticConsent", toJson(request)));
    }

    @ApiOperation("获取企业信息")
    @PostMapping("/getCorpInfo")
    public ApiResult<CorpInfoResponse> getCorpInfo(@RequestBody GetCorpInfoRequest request) {
        return ApiResult.from(client.post("/wxwork/GetCorpInfo", toJson(request)), CorpInfoResponse.class);
    }

    @ApiOperation("获取封禁详情")
    @PostMapping("/getBanDetail")
    public ApiResult<Void> getBanDetail(@RequestBody GetBanDetailRequest request) {
        return ApiResult.from(client.post("/wxwork/getBanDetile", toJson(request)));
    }

    @ApiOperation("上传图片")
    @PostMapping("/uploadImage")
    public ApiResult<Void> uploadImage(@RequestBody GetBanDetailRequest request) {
        return ApiResult.from(client.post("/wxwork/add_image", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
