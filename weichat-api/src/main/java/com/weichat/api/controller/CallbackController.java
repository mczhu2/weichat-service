package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.CallbackPullService;
import com.weichat.api.vo.request.callback.CallbackPullRequest;
import com.weichat.api.vo.response.callback.CallbackPullResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回调消息管理控制器
 *
 * @author weichat
 */
@Api(tags = "设备消息回溯接口")
@RestController
@RequestMapping("/api/v1/callback")
public class CallbackController {

    @Autowired
    private CallbackPullService callbackPullService;

    @ApiOperation("回拉消息记录")
    @PostMapping("/pull")
    public ApiResult<CallbackPullResponse> pullMessages(@RequestBody CallbackPullRequest request) {
        return callbackPullService.pullCallbackMessages(request);
    }
}
