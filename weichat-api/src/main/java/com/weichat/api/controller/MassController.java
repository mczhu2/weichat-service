package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mass")
public class MassController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/okSendGroup")
    public ApiResult okSendGroup(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/oksendgroup", params));
    }

    @PostMapping("/getGroupMsgList")
    public ApiResult getGroupMsgList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetGroupMsgList", params));
    }

    @PostMapping("/getGroupMsgInfo")
    public ApiResult getGroupMsgInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetGroupMsgInfo", params));
    }

    @PostMapping("/queryGroupSend")
    public ApiResult queryGroupSend(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/QueryGroupSendReq", params));
    }

    @PostMapping("/okSendSnsMsg")
    public ApiResult okSendSnsMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/oksendsnsmsg", params));
    }

    @PostMapping("/memberCustomerStat")
    public ApiResult memberCustomerStat(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/MemberCustomerStat", params));
    }
}
