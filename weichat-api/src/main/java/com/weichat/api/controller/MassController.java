package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.mass.*;
import com.weichat.api.vo.response.mass.GroupMsgInfo;
import com.weichat.api.vo.response.mass.GroupMsgListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 群发服务Controller
 *
 * @author weichat
 */
@Api(tags = "群发服务")
@RestController
@RequestMapping("/api/v1/mass")
public class MassController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("执行群发")
    @PostMapping("/okSendGroup")
    public ApiResult<Void> okSendGroup(@RequestBody TaskIdRequest request) {
        return ApiResult.from(client.post("/wxwork/oksendgroup", toJson(request)));
    }

    @ApiOperation("获取群发消息列表")
    @PostMapping("/getGroupMsgList")
    public ApiResult<GroupMsgListResponse> getGroupMsgList(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetGroupMsgList", toJson(request)), GroupMsgListResponse.class);
    }

    @ApiOperation("获取群发消息详情")
    @PostMapping("/getGroupMsgInfo")
    public ApiResult<GroupMsgInfo> getGroupMsgInfo(@RequestBody MsgIdRequest request) {
        return ApiResult.from(client.post("/wxwork/GetGroupMsgInfo", toJson(request)), GroupMsgInfo.class);
    }

    @ApiOperation("查询群发任务")
    @PostMapping("/queryGroupSend")
    public ApiResult<Void> queryGroupSend(@RequestBody TaskIdRequest request) {
        return ApiResult.from(client.post("/wxwork/QueryGroupSendReq", toJson(request)));
    }

    @ApiOperation("发送朋友圈群发")
    @PostMapping("/okSendSnsMsg")
    public ApiResult<Void> okSendSnsMsg(@RequestBody TaskIdRequest request) {
        return ApiResult.from(client.post("/wxwork/oksendsnsmsg", toJson(request)));
    }

    @ApiOperation("成员客户统计")
    @PostMapping("/memberCustomerStat")
    public ApiResult<Void> memberCustomerStat(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/MemberCustomerStat", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
