package com.weichat.api.strategy.impl;

import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.initializer.InitChainManager;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLoginStrategyTest {

    @Mock
    private WxUserInfoService wxUserInfoService;

    @Mock
    private InitChainManager initChainManager;

    @InjectMocks
    private UserLoginStrategy userLoginStrategy;

    @Test
    void shouldUpdateLatestUuidByVidWhenReLoginCallbackArrives() {
        CallbackRequest callbackRequest = new CallbackRequest();
        callbackRequest.setType("104001");
        callbackRequest.setUuid("new-uuid");
        callbackRequest.setJson("{\"Vid\":1688853790599424,\"user_id\":12345,\"corp_id\":67890,\"nickname\":\"test-user\"}");

        WxUserInfo existingUser = new WxUserInfo();
        existingUser.setId(99L);
        existingUser.setVid(1688853790599424L);
        existingUser.setUuid("old-uuid");
        existingUser.setUserId(12345L);
        existingUser.setCorpid(67890L);

        when(wxUserInfoService.selectByVid(eq(1688853790599424L))).thenReturn(existingUser);

        userLoginStrategy.handle(callbackRequest);

        ArgumentCaptor<WxUserInfo> captor = ArgumentCaptor.forClass(WxUserInfo.class);
        verify(wxUserInfoService).updateByPrimaryKey(captor.capture());
        verify(wxUserInfoService, never()).selectByUserIdAndCorpId(12345L, 67890L);

        WxUserInfo updatedUser = captor.getValue();
        assertNotNull(updatedUser);
        assertEquals(Long.valueOf(99L), updatedUser.getId());
        assertEquals("new-uuid", updatedUser.getUuid());
        assertEquals(Long.valueOf(1688853790599424L), updatedUser.getVid());
        assertEquals(Long.valueOf(12345L), updatedUser.getUserId());
        assertEquals(Long.valueOf(67890L), updatedUser.getCorpid());
    }

    @Test
    void shouldFallbackToUserIdAndCorpIdWhenVidIsInvalid() {
        CallbackRequest callbackRequest = new CallbackRequest();
        callbackRequest.setType("104001");
        callbackRequest.setUuid("new-uuid");
        callbackRequest.setJson("{\"Vid\":0,\"user_id\":12345,\"corp_id\":67890,\"nickname\":\"test-user\"}");

        WxUserInfo existingUser = new WxUserInfo();
        existingUser.setId(100L);
        existingUser.setUuid("old-uuid");
        existingUser.setUserId(12345L);
        existingUser.setCorpid(67890L);

        when(wxUserInfoService.selectByUserIdAndCorpId(eq(12345L), eq(67890L))).thenReturn(existingUser);

        userLoginStrategy.handle(callbackRequest);

        ArgumentCaptor<WxUserInfo> captor = ArgumentCaptor.forClass(WxUserInfo.class);
        verify(wxUserInfoService).updateByPrimaryKey(captor.capture());

        WxUserInfo updatedUser = captor.getValue();
        assertEquals(Long.valueOf(100L), updatedUser.getId());
        assertEquals("new-uuid", updatedUser.getUuid());
        assertEquals(Long.valueOf(12345L), updatedUser.getUserId());
        assertEquals(Long.valueOf(67890L), updatedUser.getCorpid());
    }
}
