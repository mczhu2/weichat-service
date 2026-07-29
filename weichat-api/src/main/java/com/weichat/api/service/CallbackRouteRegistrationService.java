package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.callback.CallbackRouteUpsertRequest;
import com.weichat.api.vo.response.callback.CallbackRouteUpsertResponse;
import com.weichat.common.entity.WxCallbackRoute;
import com.weichat.common.service.WxCallbackRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;

/**
 * 业务系统回调路由注册服务。
 * <p>保存 uuid 到业务系统回调 URL 的映射前，静默为该 uuid 注册系统固定的企微平台回调入口。</p>
 */
@Service
public class CallbackRouteRegistrationService {

    private static final String CALLBACK_TYPE_HTTP = "HTTP";
    private static final String WXWORK_SET_CALLBACK_URL_PATH = "/wxwork/SetCallbackUrl";

    @Autowired
    private WxWorkApiClient client;

    @Autowired
    private WxCallbackRouteService wxCallbackRouteService;

    @Value("${wecom.platform.callback.url:http://ai-agent.okgcc.cn/wx/wxwork/SetCallbackUrl}")
    private String platformCallbackUrl;

    /**
     * 注册企微平台固定回调入口，并保存业务系统回调路由。
     *
     * @param request 业务系统回调路由保存请求
     * @return 保存响应
     */
    public ApiResult<CallbackRouteUpsertResponse> upsertRouteAndRegisterPlatformCallback(CallbackRouteUpsertRequest request) {
        String validationMessage = validateRequest(request);
        if (StringUtils.hasText(validationMessage)) {
            return ApiResult.fail(validationMessage);
        }

        String uuid = request.getUuid().trim();
        String callbackUrl = request.getCallbackUrl().trim();
        JSONObject registerResult = registerPlatformCallback(uuid);
        ApiResult<Void> platformResult = ApiResult.from(registerResult);
        if (platformResult.getCode() != 0) {
            return ApiResult.fail("企微平台回调入口注册失败：" + platformResult.getMsg());
        }

        WxCallbackRoute route = new WxCallbackRoute();
        route.setUuid(uuid);
        route.setCallbackUrl(callbackUrl);
        wxCallbackRouteService.upsert(route);

        CallbackRouteUpsertResponse response = CallbackRouteUpsertResponse.builder()
                .uuid(uuid)
                .callbackUrl(callbackUrl)
                .platformCallbackUrl(platformCallbackUrl)
                .platformCallbackRegistered(Boolean.TRUE)
                .build();
        return ApiResult.success(response);
    }

    private JSONObject registerPlatformCallback(String uuid) {
        JSONObject payload = new JSONObject();
        payload.put("uuid", uuid);
        payload.put("url", platformCallbackUrl);
        payload.put("callbackType", CALLBACK_TYPE_HTTP);
        return client.post(WXWORK_SET_CALLBACK_URL_PATH, payload);
    }

    private String validateRequest(CallbackRouteUpsertRequest request) {
        if (request == null) {
            return "请求体不能为空";
        }
        if (!StringUtils.hasText(request.getUuid())) {
            return "uuid 不能为空";
        }
        if (!StringUtils.hasText(request.getCallbackUrl())) {
            return "业务系统回调 URL 不能为空";
        }
        if (!isHttpUrl(request.getCallbackUrl().trim())) {
            return "业务系统回调 URL 必须以 http:// 或 https:// 开头";
        }
        return null;
    }

    private boolean isHttpUrl(String url) {
        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            return ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    && StringUtils.hasText(uri.getHost());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
