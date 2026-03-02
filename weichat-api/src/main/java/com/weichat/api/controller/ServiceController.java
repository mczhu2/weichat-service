package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.service.*;
import com.weichat.api.vo.response.service.KfInfo;
import com.weichat.api.vo.response.service.KfListResponse;
import com.weichat.api.vo.response.service.WaitQueueItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客服中心Controller
 *
 * @author weichat
 */
@Api(tags = "客服中心")
@RestController
@RequestMapping("/api/v1/service")
public class ServiceController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取客服列表")
    @PostMapping("/getKFList")
    public ApiResult<KfListResponse> getKFList(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/getKFList", toJson(request)), KfListResponse.class);
    }

    @ApiOperation("获取客服信息")
    @PostMapping("/getKFInfo")
    public ApiResult<KfInfo> getKFInfo(@RequestBody KfIdRequest request) {
        return ApiResult.from(client.post("/wxwork/getKFInfo", toJson(request)), KfInfo.class);
    }

    @ApiOperation("获取客服状态")
    @PostMapping("/getKFState")
    public ApiResult<KfInfo> getKFState(@RequestBody KfIdRequest request) {
        return ApiResult.from(client.post("/wxwork/getKFState", toJson(request)), KfInfo.class);
    }

    @ApiOperation("设置客服状态")
    @PostMapping("/setKFState")
    public ApiResult<Void> setKFState(@RequestBody SetKFStateRequest request) {
        return ApiResult.from(client.post("/wxwork/setKFState", toJson(request)));
    }

    @ApiOperation("获取等待队列")
    @PostMapping("/getWaitQueue")
    public ApiResult<List<WaitQueueItem>> getWaitQueue(@RequestBody KfIdRequest request) {
        return ApiResult.fromList(client.post("/wxwork/getWxKfWaitQueueItems", toJson(request)), WaitQueueItem.class);
    }

    @ApiOperation("接入客户")
    @PostMapping("/connectWithCustomers")
    public ApiResult<Void> connectWithCustomers(@RequestBody ConnectWithCustomersRequest request) {
        return ApiResult.from(client.post("/wxwork/connectWithCustomers", toJson(request)));
    }

    @ApiOperation("获取可转接列表")
    @PostMapping("/getTransferList")
    public ApiResult<List<KfInfo>> getTransferList(@RequestBody KfIdRequest request) {
        return ApiResult.fromList(client.post("/wxwork/getWxKfSingleCanTransferList", toJson(request)), KfInfo.class);
    }

    @ApiOperation("结束聊天")
    @PostMapping("/endChat")
    public ApiResult<Void> endChat(@RequestBody ConnectWithCustomersRequest request) {
        return ApiResult.from(client.post("/wxwork/kfEndChat", toJson(request)));
    }

    @ApiOperation("切换聊天")
    @PostMapping("/switchChat")
    public ApiResult<Void> switchChat(@RequestBody SwitchChatRequest request) {
        return ApiResult.from(client.post("/wxwork/kfSwitchChat", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
