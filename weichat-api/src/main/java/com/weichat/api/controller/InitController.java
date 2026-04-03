package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.init.CheckCodeRequest;
import com.weichat.api.vo.request.init.InitRequest;
import com.weichat.api.vo.request.init.SetCallbackUrlRequest;
import com.weichat.api.vo.request.init.SetProxyRequest;
import com.weichat.api.vo.response.init.InitResponse;
import com.weichat.api.vo.response.init.QrCodeResponse;
import com.weichat.api.vo.response.init.RunClientResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 初始化登录Controller
 *
 * @author weichat
 */
@Api(tags = "初始化登录")
@RestController
@RequestMapping("/api/v1/init")
public class InitController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("初始化")
    @PostMapping("/init")
    public ApiResult<InitResponse> init(@RequestBody InitRequest request) {
        return ApiResult.from(client.post("/wxwork/init", toJson(request)), InitResponse.class);
    }

    @ApiOperation("设置回调URL")
    @PostMapping("/setCallbackUrl")
    public ApiResult<Void> setCallbackUrl(@RequestBody SetCallbackUrlRequest request) {
        return ApiResult.from(client.post("/wxwork/SetCallbackUrl", toJson(request)));
    }

    @ApiOperation("获取运行客户端")
    @PostMapping("/getRunClient")
    public ApiResult<RunClientResponse> getRunClient(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetRunClient", toJson(request)), RunClientResponse.class);
    }

    @ApiOperation("根据UUID获取运行客户端")
    @PostMapping("/getRunClientByUuid")
    public ApiResult<RunClientResponse> getRunClientByUuid(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetRunClientByUuid", toJson(request)), RunClientResponse.class);
    }

    @ApiOperation("关闭连接")
    @PostMapping("/closeConnect")
    public ApiResult<Void> closeConnect(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/CloseConnent", toJson(request)));
    }

    @ApiOperation("设置代理")
    @PostMapping("/setProxy")
    public ApiResult<Void> setProxy(@RequestBody SetProxyRequest request) {
        return ApiResult.from(client.post("/wxwork/setProxy", toJson(request)));
    }

    @ApiOperation("获取二维码")
    @PostMapping("/getQrCode")
    public ApiResult<QrCodeResponse> getQrCode(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/getQrCode", toJson(request)), QrCodeResponse.class);
    }

    @ApiOperation("检查验证码")
    @PostMapping("/checkCode")
    public ApiResult<Void> checkCode(@RequestBody CheckCodeRequest request) {
        return ApiResult.from(client.post("/wxwork/CheckCode", toJson(request)));
    }

    @ApiOperation("自动登录")
    @PostMapping("/automaticLogin")
    public ApiResult<InitResponse> automaticLogin(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/automaticLogin", toJson(request)), InitResponse.class);
    }

    @ApiOperation("登出")
    @PostMapping("/loginOut")
    public ApiResult<Void> loginOut(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/LoginOut", toJson(request)));
    }

    @ApiOperation("二次验证")
    @PostMapping("/secondaryValidation")
    public ApiResult<Void> secondaryValidation(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/SecondaryValidation", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
