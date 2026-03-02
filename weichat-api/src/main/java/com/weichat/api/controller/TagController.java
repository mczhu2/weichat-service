package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.tag.*;
import com.weichat.api.vo.response.tag.LabelInfo;
import com.weichat.api.vo.response.tag.LabelListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签管理Controller
 *
 * @author weichat
 */
@Api(tags = "标签管理")
@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取标签列表")
    @PostMapping("/getLabelList")
    public ApiResult<LabelListResponse> getLabelList(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetLabelListReq", toJson(request)), LabelListResponse.class);
    }

    @ApiOperation("添加标签")
    @PostMapping("/addLabel")
    public ApiResult<LabelInfo> addLabel(@RequestBody AddLabelRequest request) {
        return ApiResult.from(client.post("/wxwork/AddLabelReq", toJson(request)), LabelInfo.class);
    }

    @ApiOperation("编辑标签")
    @PostMapping("/editLabel")
    public ApiResult<Void> editLabel(@RequestBody EditLabelRequest request) {
        return ApiResult.from(client.post("/wxwork/EditLabelReq", toJson(request)));
    }

    @ApiOperation("删除标签")
    @PostMapping("/delLabel")
    public ApiResult<Void> delLabel(@RequestBody DelLabelRequest request) {
        return ApiResult.from(client.post("/wxwork/DelLabelReq", toJson(request)));
    }

    @ApiOperation("标签添加用户")
    @PostMapping("/labelAddUser")
    public ApiResult<Void> labelAddUser(@RequestBody LabelAddUserRequest request) {
        return ApiResult.from(client.post("/wxwork/LabelAddUserReq", toJson(request)));
    }

    @ApiOperation("用户添加标签")
    @PostMapping("/userAddLabels")
    public ApiResult<Void> userAddLabels(@RequestBody UserAddLabelsRequest request) {
        return ApiResult.from(client.post("/wxwork/UserAddLabelsReq", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
