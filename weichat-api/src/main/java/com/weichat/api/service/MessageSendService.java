package com.weichat.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.message.SendAppMessageRequest;
import com.weichat.api.vo.request.message.SendAppRequest;
import com.weichat.api.vo.request.message.SendImageRequest;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.api.vo.response.message.SendMsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MessageSendService {

    @Autowired
    private WxWorkApiClient client;

    @Autowired
    private AsyncMessagePersistenceService asyncMessagePersistenceService;

    @Autowired
    private CdnFileService cdnFileService;

    public ApiResult<SendMsgResponse> sendText(SendTextRequest request) {
        JSONObject response = client.post("/wxwork/SendTextMsg", JSON.parseObject(JSON.toJSONString(request)));
        asyncMessagePersistenceService.saveSentTextMessage(request, response);
        return ApiResult.from(response, SendMsgResponse.class);
    }

    /**
     * 发送 CDN 图片消息，调用方需提前完成图片上传并填充完整媒体信息。
     */
    public ApiResult<SendMsgResponse> sendImage(SendImageRequest request) {
        JSONObject response = client.post("/wxwork/SendCDNImgMsg", JSON.parseObject(JSON.toJSONString(request)));
        return ApiResult.from(response, SendMsgResponse.class);
    }

    /**
     * 发送 CDN 语音消息，调用方需提前完成语音上传并填充完整媒体信息。
     */
    public ApiResult<SendMsgResponse> sendVoice(SendVoiceRequest request) {
        JSONObject response = client.post("/wxwork/SendCDNVoiceMsg", JSON.parseObject(JSON.toJSONString(request)));
        return ApiResult.from(response, SendMsgResponse.class);
    }

    /**
     * 发送小程序消息。接口层请求使用 coverUrl，实际调用前需要先上传封面得到 CDN 信息。
     */
    public ApiResult<SendMsgResponse> sendApp(SendAppMessageRequest request) {
        CdnUploadResponse uploadResponse = validateAppCoverUploadResponse(
                cdnFileService.uploadImageByUrl(request.getUuid(), request.getCoverUrl(), null, null)
        );

        SendAppRequest sendAppRequest = SendAppRequest.builder()
                .uuid(request.getUuid())
                .send_userid(request.getSend_userid())
                .isRoom(request.getIsRoom() != null ? request.getIsRoom() : Boolean.FALSE)
                .title(requireText(request.getTitle(), "app title is required"))
                .desc(requireText(request.getDesc(), "app desc is required"))
                .appName(requireText(request.getAppName(), "appName is required"))
                .appid(requireText(request.getAppid(), "appid is required"))
                .username(requireText(request.getUsername(), "username is required"))
                .pagepath(requireText(request.getPagepath(), "pagepath is required"))
                .weappIconUrl(request.getWeappIconUrl())
                .cdnkey(requireText(resolveCdnKey(uploadResponse), "app cover cdnkey is required"))
                .aeskey(requireText(uploadResponse.getAes_key(), "app cover aeskey is required"))
                .md5(requireText(uploadResponse.getMd5(), "app cover md5 is required"))
                .fileSize(requireInteger(uploadResponse.getSize(), "app cover fileSize is required"))
                .build();
        JSONObject response = client.post("/wxwork/SendAppMsg", JSON.parseObject(JSON.toJSONString(sendAppRequest)));
        return ApiResult.from(response, SendMsgResponse.class);
    }

    private CdnUploadResponse validateAppCoverUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null) {
            throw new IllegalStateException("CDN app cover upload returned null data");
        }
        if (!StringUtils.hasText(resolveCdnKey(uploadResponse))) {
            throw new IllegalStateException("CDN app cover upload response is missing cdn_key/fileid");
        }
        if (!StringUtils.hasText(uploadResponse.getAes_key())) {
            throw new IllegalStateException("CDN app cover upload response is missing aes_key");
        }
        if (!StringUtils.hasText(uploadResponse.getMd5())) {
            throw new IllegalStateException("CDN app cover upload response is missing md5");
        }
        if (uploadResponse.getSize() == null) {
            throw new IllegalStateException("CDN app cover upload response is missing size");
        }
        return uploadResponse;
    }

    private String resolveCdnKey(CdnUploadResponse uploadResponse) {
        if (StringUtils.hasText(uploadResponse.getCdn_key())) {
            return uploadResponse.getCdn_key();
        }
        return uploadResponse.getFileid();
    }

    private String requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private Integer requireInteger(Integer value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
