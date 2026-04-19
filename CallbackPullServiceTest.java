import com.weichat.api.service.CallbackPullService;
import com.weichat.api.vo.request.callback.CallbackPullRequest;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.mapper.WxCallbackTaskMapper;
import com.weichat.common.mapper.WxUserInfoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallbackPullServiceTest {

    @Mock
    private WxUserInfoMapper wxUserInfoMapper;

    @Mock
    private WxCallbackTaskMapper wxCallbackTaskMapper;

    @InjectMocks
    private CallbackPullService callbackPullService;

    @Test
    void testPullCallbackMessages_WithIncrementalData_LastPulledMessageIdIsNull() {
        // 准备测试数据
        String uuid = "test-uuid";
        CallbackPullRequest request = new CallbackPullRequest();
        request.setUuid(uuid);
        request.setDataRangeType(1); // 增量数据
        
        WxUserInfo mockUserInfo = new WxUserInfo();
        mockUserInfo.setLastPulledMessageId(null); // 模拟空值情况
        
        // 模拟获取最近5分钟的最小ID
        Long minIdFromLastFiveMinutes = 100L;
        
        when(wxUserInfoMapper.selectByUuid(eq(uuid))).thenReturn(mockUserInfo);
        when(wxCallbackTaskMapper.selectMinIdAfterTime(eq(uuid), any(Date.class))).thenReturn(minIdFromLastFiveMinutes);
        
        // 执行测试
        callbackPullService.pullCallbackMessages(request);
        
        // 验证是否调用了获取最近5分钟最小ID的方法
        verify(wxCallbackTaskMapper, times(1)).selectMinIdAfterTime(eq(uuid), any(Date.class));
    }
    
    @Test
    void testPullCallbackMessages_WithIncrementalData_LastPulledMessageIdNotNull() {
        // 准备测试数据
        String uuid = "test-uuid";
        CallbackPullRequest request = new CallbackPullRequest();
        request.setUuid(uuid);
        request.setDataRangeType(1); // 增量数据
        
        Long lastPulledMessageId = 50L;
        WxUserInfo mockUserInfo = new WxUserInfo();
        mockUserInfo.setLastPulledMessageId(lastPulledMessageId); // 模拟非空值情况
        
        when(wxUserInfoMapper.selectByUuid(eq(uuid))).thenReturn(mockUserInfo);
        
        // 执行测试
        callbackPullService.pullCallbackMessages(request);
        
        // 验证没有调用获取最近5分钟最小ID的方法
        verify(wxCallbackTaskMapper, never()).selectMinIdAfterTime(any(String.class), any(Date.class));
    }
    
    @Test
    void testPullCallbackMessages_WithFullData() {
        // 准备测试数据
        String uuid = "test-uuid";
        CallbackPullRequest request = new CallbackPullRequest();
        request.setUuid(uuid);
        request.setDataRangeType(0); // 全量数据
        
        WxUserInfo mockUserInfo = new WxUserInfo();
        mockUserInfo.setLastPulledMessageId(null); // 模拟空值情况
        
        when(wxUserInfoMapper.selectByUuid(eq(uuid))).thenReturn(mockUserInfo);
        
        // 执行测试
        callbackPullService.pullCallbackMessages(request);
        
        // 验证没有调用获取最近5分钟最小ID的方法
        verify(wxCallbackTaskMapper, never()).selectMinIdAfterTime(any(String.class), any(Date.class));
    }
}