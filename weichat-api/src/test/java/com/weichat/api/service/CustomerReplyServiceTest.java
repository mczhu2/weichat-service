package com.weichat.api.service;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.api.vo.response.message.SendMsgResponse;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerReplyServiceTest {

    @Mock
    private MessageSendService messageSendService;

    @Mock
    private CdnImageService cdnImageService;

    @Mock
    private CdnFileService cdnFileService;

    @Mock
    private WxUserInfoService wxUserInfoService;

    @InjectMocks
    private CustomerReplyService customerReplyService;

    @Test
    void shouldSendVoiceAndTextWhenCallbackContainsVoiceAndText() {
        WxMessageInfo wxMessageInfo = new WxMessageInfo();
        wxMessageInfo.setMsgId(1001L);
        wxMessageInfo.setReceiver(2002L);
        wxMessageInfo.setSender(3003L);

        WxUserInfo receiverUser = new WxUserInfo();
        receiverUser.setUuid("uuid-voice");

        CdnUploadResponse uploadResponse = new CdnUploadResponse();
        uploadResponse.setCdn_key("cdn-key");
        uploadResponse.setAes_key("aes-key");
        uploadResponse.setMd5("md5-value");
        uploadResponse.setSize(1234);

        when(cdnFileService.uploadFile(any(), any())).thenReturn(uploadResponse);
        when(messageSendService.sendVoice(any(SendVoiceRequest.class))).thenReturn(ApiResult.success(new SendMsgResponse()));

        String callbackBody = "{\"ok\":true,\"reply\":\"text should be ignored\",\"voices\":[{\"url\":\"https://example.com/reply.silk\",\"voice_time\":7,\"contentType\":\"audio/silk\"}]}";

        customerReplyService.sendReplyToCustomer(wxMessageInfo, receiverUser, callbackBody);

        ArgumentCaptor<SendVoiceRequest> voiceCaptor = ArgumentCaptor.forClass(SendVoiceRequest.class);
        verify(messageSendService).sendVoice(voiceCaptor.capture());
        ArgumentCaptor<SendTextRequest> textCaptor = ArgumentCaptor.forClass(SendTextRequest.class);
        verify(messageSendService).sendText(textCaptor.capture());
        assertEquals("uuid-voice", voiceCaptor.getValue().getUuid());
        assertEquals(3003L, voiceCaptor.getValue().getSend_userid());
        assertEquals(7, voiceCaptor.getValue().getVoice_time());
        assertEquals("text should be ignored", textCaptor.getValue().getContent());
    }

    @Test
    void shouldSendTextReplyWhenCallbackContainsOnlyText() {
        WxMessageInfo wxMessageInfo = new WxMessageInfo();
        wxMessageInfo.setMsgId(4004L);
        wxMessageInfo.setReceiver(5005L);
        wxMessageInfo.setSender(6006L);

        WxUserInfo receiverUser = new WxUserInfo();
        receiverUser.setUuid("uuid-text");

        when(messageSendService.sendText(any(SendTextRequest.class))).thenReturn(ApiResult.success(new SendMsgResponse()));

        String callbackBody = "{\"ok\":true,\"reply\":\"hello text only\"}";

        customerReplyService.sendReplyToCustomer(wxMessageInfo, receiverUser, callbackBody);

        ArgumentCaptor<SendTextRequest> textCaptor = ArgumentCaptor.forClass(SendTextRequest.class);
        verify(messageSendService).sendText(textCaptor.capture());
        verify(messageSendService, never()).sendVoice(any(SendVoiceRequest.class));
        assertEquals("uuid-text", textCaptor.getValue().getUuid());
        assertEquals(6006L, textCaptor.getValue().getSend_userid());
        assertEquals("hello text only", textCaptor.getValue().getContent());
    }

    @Test
    void shouldSendVoiceAndTextForDirectFriendReplyRequest() {
        ReplyMediaItem voice = new ReplyMediaItem();
        voice.setUrl("https://example.com/direct-reply.silk");
        voice.setVoiceTime(9);
        voice.setContentType("audio/silk");

        CdnUploadResponse uploadResponse = new CdnUploadResponse();
        uploadResponse.setCdn_key("cdn-direct");
        uploadResponse.setAes_key("aes-direct");
        uploadResponse.setMd5("md5-direct");
        uploadResponse.setSize(4321);

        when(cdnFileService.uploadFile(any(), any())).thenReturn(uploadResponse);
        when(messageSendService.sendVoice(any(SendVoiceRequest.class))).thenReturn(ApiResult.success(new SendMsgResponse()));

        customerReplyService.sendFriendReply(
                com.weichat.api.vo.request.message.SendFriendReplyRequest.builder()
                        .uuid("uuid-direct")
                        .sender(7007L)
                        .receiver(8008L)
                        .reply("direct text should be ignored")
                        .voices(java.util.Collections.singletonList(voice))
                        .build()
        );

        verify(messageSendService).sendVoice(any(SendVoiceRequest.class));
        verify(messageSendService, atLeastOnce()).sendText(any(SendTextRequest.class));
    }
}
