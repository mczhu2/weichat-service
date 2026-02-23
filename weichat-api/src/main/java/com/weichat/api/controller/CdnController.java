package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cdn")
public class CdnController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getBigAuthkey")
    public ApiResult getBigAuthkey(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetBigAuthkey", params));
    }

    @PostMapping("/bigFileUploadLink")
    public ApiResult bigFileUploadLink(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/BigFileUploadLink", params));
    }

    @PostMapping("/bigUploadFile")
    public ApiResult bigUploadFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/BigUploadFile", params));
    }

    @PostMapping("/bigFileDownUrl")
    public ApiResult bigFileDownUrl(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/bigFileDownUrl", params));
    }

    @PostMapping("/getBigFileDownData")
    public ApiResult getBigFileDownData(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getBigFileDownData", params));
    }

    @PostMapping("/cdnUploadImgLink")
    public ApiResult cdnUploadImgLink(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CdnUploadImgLink", params));
    }

    @PostMapping("/cdnUploadImg")
    public ApiResult cdnUploadImg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CdnUploadImg", params));
    }

    @PostMapping("/uploadCdnVideoLink")
    public ApiResult uploadCdnVideoLink(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/UploadCdnVideoLink", params));
    }

    @PostMapping("/cdnUploadVideo")
    public ApiResult cdnUploadVideo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CdnUploadVideo", params));
    }

    @PostMapping("/uploadCdnLinkFile")
    public ApiResult uploadCdnLinkFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/UploadCdnLinkFile", params));
    }

    @PostMapping("/cdnUploadFile")
    public ApiResult cdnUploadFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CdnUploadFile", params));
    }

    @PostMapping("/downloadFile")
    public ApiResult downloadFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DownloadFile", params));
    }

    @PostMapping("/downloadWeChatFile")
    public ApiResult downloadWeChatFile(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DownloadWeChatFile", params));
    }

    @PostMapping("/getCdnInfo")
    public ApiResult getCdnInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetCdnInfo", params));
    }

    @PostMapping("/initCdn")
    public ApiResult initCdn(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/InitCdn", params));
    }
}
