package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.callback.CallbackRouteUpsertRequest;
import com.weichat.api.vo.response.callback.CallbackRouteUpsertResponse;
import com.weichat.common.entity.WxCallbackRoute;
import com.weichat.common.service.WxCallbackRouteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CallbackRouteRegistrationServiceTest {

    @Mock
    private WxWorkApiClient client;

    @Mock
    private WxCallbackRouteService wxCallbackRouteService;

    @InjectMocks
    private CallbackRouteRegistrationService callbackRouteRegistrationService;

    @Test
    void shouldRegisterFixedPlatformCallbackAndUpsertRouteForValidRequest() {
        CallbackRouteUpsertRequest request = new CallbackRouteUpsertRequest();
        request.setUuid(" uuid-route-123 ");
        request.setCallbackUrl(" https://tenant.example.com/wecom/callback ");

        ReflectionTestUtils.setField(
                callbackRouteRegistrationService,
                "platformCallbackUrl",
                "https://platform.example.com/wxwork/callback"
        );

        JSONObject registerResponse = new JSONObject();
        registerResponse.put("errcode", 0);
        registerResponse.put("errmsg", "ok");
        when(client.post(eq("/wxwork/SetCallbackUrl"), org.mockito.ArgumentMatchers.any(JSONObject.class)))
                .thenReturn(registerResponse);

        ApiResult<CallbackRouteUpsertResponse> result =
                callbackRouteRegistrationService.upsertRouteAndRegisterPlatformCallback(request);

        ArgumentCaptor<JSONObject> payloadCaptor = ArgumentCaptor.forClass(JSONObject.class);
        verify(client).post(eq("/wxwork/SetCallbackUrl"), payloadCaptor.capture());
        JSONObject platformPayload = payloadCaptor.getValue();
        assertEquals("uuid-route-123", platformPayload.getString("uuid"));
        assertEquals("https://platform.example.com/wxwork/callback", platformPayload.getString("url"));
        assertEquals("HTTP", platformPayload.getString("callbackType"));

        ArgumentCaptor<WxCallbackRoute> routeCaptor = ArgumentCaptor.forClass(WxCallbackRoute.class);
        verify(wxCallbackRouteService).upsert(routeCaptor.capture());
        WxCallbackRoute route = routeCaptor.getValue();
        assertEquals("uuid-route-123", route.getUuid());
        assertEquals("https://tenant.example.com/wecom/callback", route.getCallbackUrl());

        assertEquals(0, result.getCode());
        assertEquals("uuid-route-123", result.getData().getUuid());
        assertEquals("https://tenant.example.com/wecom/callback", result.getData().getCallbackUrl());
        assertEquals("https://platform.example.com/wxwork/callback", result.getData().getPlatformCallbackUrl());
        assertTrue(result.getData().getPlatformCallbackRegistered());
    }

    @Test
    void shouldFailInvalidCallbackUrlWithoutCallingDependencies() {
        CallbackRouteUpsertRequest request = new CallbackRouteUpsertRequest();
        request.setUuid("uuid-route-123");
        request.setCallbackUrl("ftp://tenant.example.com/wecom/callback");

        ApiResult<CallbackRouteUpsertResponse> result =
                callbackRouteRegistrationService.upsertRouteAndRegisterPlatformCallback(request);

        assertEquals(-1, result.getCode());
        assertTrue(result.getMsg().contains("http:// 或 https://"));
        assertFalse(result.getMsg().isEmpty());
        verifyNoInteractions(client, wxCallbackRouteService);
    }
}
