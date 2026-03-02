package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.chattag.*;
import com.weichat.api.vo.response.chattag.SessionTagInfo;
import com.weichat.api.vo.response.chattag.SessionTagListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会话标签Controller
 *
 * @author weichat
 */
@Api(tags = "会话标签")
@RestController
@RequestMapping("/api/v1/chattag")
public class ChatTagController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("修改会话标签")
    @PostMapping("/modSessionTag")
    public ApiResult<Void> modSessionTag(@RequestBody ModSessionTagRequest request) {
        return ApiResult.from(client.post("/wxwork/ModSessionTag", toJson(request)));
    }

    @ApiOperation("获取会话标签列表")
    @PostMapping("/getSessionTagList")
    public ApiResult<SessionTagListResponse> getSessionTagList(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetSessionTagList", toJson(request)), SessionTagListResponse.class);
    }

    @ApiOperation("获取会话标签内容")
    @PostMapping("/getSessionInTag")
    public ApiResult<List<SessionTagInfo>> getSessionInTag(@RequestBody TagIdRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetSessionInTagReq", toJson(request)), SessionTagInfo.class);
    }

    @ApiOperation("会话标签操作")
    @PostMapping("/sessionInTag")
    public ApiResult<Void> sessionInTag(@RequestBody SessionInTagRequest request) {
        return ApiResult.from(client.post("/wxwork/SessionInTagReq", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
