package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.cdn.*;
import com.weichat.api.vo.response.cdn.CdnDownloadResponse;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CDN素材Controller
 *
 * @author weichat
 */
@Api(tags = "CDN素材")
@RestController
@RequestMapping("/api/v1/cdn")
public class CdnController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取大文件授权密钥")
    @PostMapping("/getBigAuthkey")
    public ApiResult<Void> getBigAuthkey(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetBigAuthkey", toJson(request)));
    }

    @ApiOperation("大文件上传链接")
    @PostMapping("/bigFileUploadLink")
    public ApiResult<CdnUploadResponse> bigFileUploadLink(@RequestBody BigFileUploadRequest request) {
        return ApiResult.from(client.post("/wxwork/BigFileUploadLink", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("上传大文件")
    @PostMapping("/bigUploadFile")
    public ApiResult<CdnUploadResponse> bigUploadFile(@RequestBody BigFileUploadRequest request) {
        return ApiResult.from(client.post("/wxwork/BigUploadFile", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("大文件下载URL")
    @PostMapping("/bigFileDownUrl")
    public ApiResult<CdnDownloadResponse> bigFileDownUrl(@RequestBody CdnKeyRequest request) {
        return ApiResult.from(client.post("/wxwork/bigFileDownUrl", toJson(request)), CdnDownloadResponse.class);
    }

    @ApiOperation("获取大文件下载数据")
    @PostMapping("/getBigFileDownData")
    public ApiResult<CdnDownloadResponse> getBigFileDownData(@RequestBody CdnKeyRequest request) {
        return ApiResult.from(client.post("/wxwork/getBigFileDownData", toJson(request)), CdnDownloadResponse.class);
    }

    @ApiOperation("CDN上传图片链接")
    @PostMapping("/cdnUploadImgLink")
    public ApiResult<CdnUploadResponse> cdnUploadImgLink(@RequestBody CdnUploadImgRequest request) {
        return ApiResult.from(client.post("/wxwork/CdnUploadImgLink", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("上传图片到CDN")
    @PostMapping("/cdnUploadImg")
    public ApiResult<CdnUploadResponse> cdnUploadImg(@RequestBody CdnUploadImgRequest request) {
        return ApiResult.from(client.post("/wxwork/CdnUploadImg", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("CDN上传视频链接")
    @PostMapping("/uploadCdnVideoLink")
    public ApiResult<CdnUploadResponse> uploadCdnVideoLink(@RequestBody CdnUploadVideoRequest request) {
        return ApiResult.from(client.post("/wxwork/UploadCdnVideoLink", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("上传视频到CDN")
    @PostMapping("/cdnUploadVideo")
    public ApiResult<CdnUploadResponse> cdnUploadVideo(@RequestBody CdnUploadVideoRequest request) {
        return ApiResult.from(client.post("/wxwork/CdnUploadVideo", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("CDN上传文件链接")
    @PostMapping("/uploadCdnLinkFile")
    public ApiResult<CdnUploadResponse> uploadCdnLinkFile(@RequestBody CdnUploadFileRequest request) {
        return ApiResult.from(client.post("/wxwork/UploadCdnLinkFile", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("上传文件到CDN")
    @PostMapping("/cdnUploadFile")
    public ApiResult<CdnUploadResponse> cdnUploadFile(@RequestBody CdnUploadFileRequest request) {
        return ApiResult.from(client.post("/wxwork/CdnUploadFile", toJson(request)), CdnUploadResponse.class);
    }

    @ApiOperation("下载文件")
    @PostMapping("/downloadFile")
    public ApiResult<CdnDownloadResponse> downloadFile(@RequestBody DownloadFileRequest request) {
        return ApiResult.from(client.post("/wxwork/DownloadFile", toJson(request)), CdnDownloadResponse.class);
    }

    @ApiOperation("下载微信文件")
    @PostMapping("/downloadWeChatFile")
    public ApiResult<CdnDownloadResponse> downloadWeChatFile(@RequestBody DownloadWeChatFileRequest request) {
        return ApiResult.from(client.post("/wxwork/DownloadWeChatFile", toJson(request)), CdnDownloadResponse.class);
    }

    @ApiOperation("获取CDN信息")
    @PostMapping("/getCdnInfo")
    public ApiResult<Void> getCdnInfo(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetCdnInfo", toJson(request)));
    }

    @ApiOperation("初始化CDN")
    @PostMapping("/initCdn")
    public ApiResult<Void> initCdn(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/InitCdn", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
