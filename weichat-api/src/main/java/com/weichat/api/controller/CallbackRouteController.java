package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.CallbackRouteRegistrationService;
import com.weichat.api.vo.request.callback.CallbackRouteUpsertRequest;
import com.weichat.api.vo.response.callback.CallbackRouteUpsertResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务系统回调路由控制器。
 */
@Api(tags = "业务系统回调路由")
@RestController
@RequestMapping("/api/v2/callback/routes")
public class CallbackRouteController {

    @Autowired
    private CallbackRouteRegistrationService callbackRouteRegistrationService;

    /**
     * 保存 uuid 对应的业务系统回调地址，并静默注册企微平台固定回调入口。
     *
     * @param request 路由保存请求
     * @return 路由保存结果
     */
    @ApiOperation("保存业务系统回调路由并注册企微平台回调入口")
    @PostMapping
    public ApiResult<CallbackRouteUpsertResponse> upsertRoute(@RequestBody CallbackRouteUpsertRequest request) {
        return callbackRouteRegistrationService.upsertRouteAndRegisterPlatformCallback(request);
    }
}
