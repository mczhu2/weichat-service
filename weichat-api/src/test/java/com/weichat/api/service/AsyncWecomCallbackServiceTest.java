package com.weichat.api.service;

import com.weichat.api.vo.callback.DownstreamCallbackPayload;
import com.weichat.api.vo.callback.DownstreamMediaVo;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsyncWecomCallbackServiceTest {

    @Mock
    private WxUserInfoService wxUserInfoService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DownstreamMessageContentService downstreamMessageContentService;

    @Mock
    private CustomerReplyService customerReplyService;

    @InjectMocks
    private AsyncWecomCallbackService asyncWecomCallbackService;

    @Test
    void shouldConsumeDownstreamResponseBodyForReplyDispatch() {
        WxMessageInfo wxMessageInfo = new WxMessageInfo();
        wxMessageInfo.setMsgId(1013547L);
        wxMessageInfo.setReceiver(1688856528881593L);
        wxMessageInfo.setSender(7881301772935700L);
        wxMessageInfo.setSenderName("sender-name");
        wxMessageInfo.setMsgtype(1);

        WxUserInfo receiverUser = new WxUserInfo();
        receiverUser.setUserId(wxMessageInfo.getReceiver());
        receiverUser.setUuid("uuid-123");

        DownstreamCallbackPayload payload = new DownstreamCallbackPayload();
        payload.setContent("hello");

        String responseBody = "{\"ok\":true,\"voices\":[{\"url\":\"https://example.com/reply.silk\",\"voice_time\":19}]}";

        ReflectionTestUtils.setField(asyncWecomCallbackService, "wecomCallbackUrl", "http://example.com/callback");

        when(wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver())).thenReturn(receiverUser);
        when(downstreamMessageContentService.resolveCallbackPayload(wxMessageInfo, receiverUser.getUuid())).thenReturn(payload);
        when(restTemplate.postForEntity(
                eq("http://example.com/callback"),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(ResponseEntity.ok(responseBody));

        asyncWecomCallbackService.dispatch(wxMessageInfo);

        verify(customerReplyService).sendReplyToCustomer(wxMessageInfo, receiverUser, responseBody);

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(eq("http://example.com/callback"), entityCaptor.capture(), eq(String.class));
        assertTrue(entityCaptor.getValue().getBody().toString().contains("\"replyReceiver\":7881301772935700"));
        assertTrue(entityCaptor.getValue().getBody().toString().contains("\"replyModalities\":[\"text\"]"));
    }

    @Test
    void shouldRequestAudioOnlyReplyForVoiceMessage() {
        WxMessageInfo wxMessageInfo = new WxMessageInfo();
        wxMessageInfo.setMsgId(20240514L);
        wxMessageInfo.setReceiver(1688856528881593L);
        wxMessageInfo.setSender(7881301772935700L);
        wxMessageInfo.setSenderName("voice-sender");
        wxMessageInfo.setMsgtype(16);

        WxUserInfo receiverUser = new WxUserInfo();
        receiverUser.setUserId(wxMessageInfo.getReceiver());
        receiverUser.setUuid("uuid-voice");

        DownstreamMediaVo voiceMedia = new DownstreamMediaVo();
        voiceMedia.setMediaType("voice");
        voiceMedia.setMediaUrl("https://example.com/input.silk");
        voiceMedia.setDuration(8);

        DownstreamCallbackPayload payload = new DownstreamCallbackPayload();
        payload.setContent("这是语音转写后的文本");
        payload.setMedias(Collections.singletonList(voiceMedia));

        when(wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver())).thenReturn(receiverUser);
        when(downstreamMessageContentService.resolveCallbackPayload(wxMessageInfo, receiverUser.getUuid())).thenReturn(payload);
        when(restTemplate.postForEntity(
                eq("http://example.com/callback"),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(ResponseEntity.ok("{\"ok\":true,\"reply\":\"ignored\"}"));

        ReflectionTestUtils.setField(asyncWecomCallbackService, "wecomCallbackUrl", "http://example.com/callback");

        asyncWecomCallbackService.dispatch(wxMessageInfo);

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(eq("http://example.com/callback"), entityCaptor.capture(), eq(String.class));
        String requestBody = entityCaptor.getValue().getBody().toString();
        assertTrue(requestBody.contains("\"msgType\":16"));
        assertTrue(requestBody.contains("\"replyModalities\":[\"audio\"]"));
    }
}
