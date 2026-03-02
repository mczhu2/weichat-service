package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.moment.*;
import com.weichat.api.vo.response.moment.SnsInfo;
import com.weichat.api.vo.response.moment.SnsListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 朋友圈Controller
 *
 * @author weichat
 */
@Api(tags = "朋友圈")
@RestController
@RequestMapping("/api/v1/moment")
public class MomentController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取朋友圈列表")
    @PostMapping("/getSnsList")
    public ApiResult<SnsListResponse> getSnsList(@RequestBody GetSnsListRequest request) {
        return ApiResult.from(client.post("/wxwork/GetSnsList", toJson(request)), SnsListResponse.class);
    }

    @ApiOperation("获取朋友圈详情")
    @PostMapping("/getSnsInfo")
    public ApiResult<SnsInfo> getSnsInfo(@RequestBody SnsIdRequest request) {
        return ApiResult.from(client.post("/wxwork/GetSnsInfo", toJson(request)), SnsInfo.class);
    }

    @ApiOperation("发布朋友圈")
    @PostMapping("/postPyq")
    public ApiResult<SnsInfo> postPyq(@RequestBody PostPyqRequest request) {
        return ApiResult.from(client.post("/wxwork/PostPyq", toJson(request)), SnsInfo.class);
    }

    @ApiOperation("删除朋友圈")
    @PostMapping("/deleteSns")
    public ApiResult<Void> deleteSns(@RequestBody SnsIdRequest request) {
        return ApiResult.from(client.post("/wxwork/DeleteSns", toJson(request)));
    }

    @ApiOperation("评论朋友圈")
    @PostMapping("/comment")
    public ApiResult<Void> comment(@RequestBody CommentSnsRequest request) {
        return ApiResult.from(client.post("/wxwork/CommentSnsReq", toJson(request)));
    }

    @ApiOperation("删除评论")
    @PostMapping("/deleteComment")
    public ApiResult<Void> deleteComment(@RequestBody DeleteCommentRequest request) {
        return ApiResult.from(client.post("/wxwork/DeleteSnsComment", toJson(request)));
    }

    @ApiOperation("点赞/取消点赞")
    @PostMapping("/likeOrUnlike")
    public ApiResult<Void> likeOrUnlike(@RequestBody LikeOrUnlikeRequest request) {
        return ApiResult.from(client.post("/wxwork/LikeOrUnLikeSns", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
