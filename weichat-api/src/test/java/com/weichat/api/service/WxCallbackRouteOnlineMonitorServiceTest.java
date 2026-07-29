package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.common.dto.WxCallbackRouteMonitorTarget;
import com.weichat.common.service.WxUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxCallbackRouteOnlineMonitorServiceTest {

    private static final String ONLINE_RESPONSE = "{\"code\":0,\"data\":{\"loginType\":2,\"user_info\":{\"isLogin\":true}}}";
    private static final String NON_ZERO_CODE_RESPONSE = "{\"code\":1,\"data\":{\"loginType\":2,\"user_info\":{\"isLogin\":true}}}";
    private static final String NON_ONLINE_LOGIN_TYPE_RESPONSE = "{\"code\":0,\"data\":{\"loginType\":1,\"user_info\":{\"isLogin\":true}}}";
    private static final String NOT_LOGGED_IN_RESPONSE = "{\"code\":0,\"data\":{\"loginType\":2,\"user_info\":{\"isLogin\":false}}}";
    private static final String RUN_CLIENT_PATH = "/wxwork/GetRunClientByUuid";
    private static final String FEISHU_WEBHOOK_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/a1693cda-b8a2-49d3-b2fc-028019d16fad";

    @Mock
    private WxUserInfoService wxUserInfoService;

    @Mock
    private WxWorkApiClient wxWorkApiClient;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WxCallbackRouteOnlineMonitorService monitorService;

    @Test
    void shouldReturnOnlineOnlyWhenCodeLoginTypeAndLoginStateMatch() {
        when(wxWorkApiClient.post(eq(RUN_CLIENT_PATH), any(JSONObject.class)))
                .thenReturn(
                        JSONObject.parseObject(ONLINE_RESPONSE),
                        JSONObject.parseObject(NON_ZERO_CODE_RESPONSE),
                        JSONObject.parseObject(NON_ONLINE_LOGIN_TYPE_RESPONSE),
                        JSONObject.parseObject(NOT_LOGGED_IN_RESPONSE)
                );

        assertTrue(monitorService.isOnline("uuid-online"));
        assertFalse(monitorService.isOnline("uuid-non-zero-code"));
        assertFalse(monitorService.isOnline("uuid-non-online-login-type"));
        assertFalse(monitorService.isOnline("uuid-not-logged-in"));
    }

    @Test
    void shouldPostFeishuTextContainingNicknameAndUuidWhenRouteTargetIsOffline() {
        WxCallbackRouteMonitorTarget target = new WxCallbackRouteMonitorTarget();
        target.setUuid("uuid-offline-001");
        target.setUserId(1688856528881593L);
        target.setNickname("离线用户昵称");

        when(wxUserInfoService.selectCallbackRouteMonitorTargets()).thenReturn(Collections.singletonList(target));
        when(wxWorkApiClient.post(eq(RUN_CLIENT_PATH), any(JSONObject.class)))
                .thenReturn(JSONObject.parseObject(NOT_LOGGED_IN_RESPONSE));
        when(restTemplate.exchange(
                eq(FEISHU_WEBHOOK_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(ResponseEntity.ok("{\"StatusCode\":0}"));

        monitorService.checkAndNotifyOfflineRoutes();

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(
                eq(FEISHU_WEBHOOK_URL),
                eq(HttpMethod.POST),
                entityCaptor.capture(),
                eq(String.class)
        );

        String requestBody = entityCaptor.getValue().getBody().toString();
        assertTrue(requestBody.contains("\"msg_type\":\"text\""));
        assertTrue(requestBody.contains("离线用户昵称"));
        assertTrue(requestBody.contains("uuid-offline-001"));
    }
}
