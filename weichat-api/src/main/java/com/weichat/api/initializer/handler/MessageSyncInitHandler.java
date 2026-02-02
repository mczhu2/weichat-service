package com.weichat.api.initializer.handler;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.initializer.AbstractInitHandler;
import com.weichat.api.initializer.InitContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class MessageSyncInitHandler extends AbstractInitHandler {
    
    private static final int PAGE_SIZE = 1000;
    
    @Autowired
    private WxWorkApiClient apiClient;
    
    @Override
    public int getOrder() {
        return 4;
    }
    
    @Override
    protected String getHandlerName() {
        return "消息记录同步";
    }
    
    @Override
    protected void doHandle(InitContext context) {
        long seq = 0;
        int syncCount = 0;
        
        while (true) {
            JSONObject result = apiClient.syncMessage(
                context.getUuid(), PAGE_SIZE, seq);
            
            if (result == null || result.getIntValue("errcode") != 0) {
                logger.warn("同步消息失败");
                break;
            }
            
            JSONObject data = result.getJSONObject("data");
            int isSelect = data.getIntValue("is_select");
            seq = data.getLongValue("seq");
            syncCount++;
            
            logger.info("消息同步批次{}完成，seq: {}", syncCount, seq);
            
            if (isSelect == 0) {
                break;
            }
            
            if (syncCount >= 10) {
                logger.info("达到最大同步批次，停止同步");
                break;
            }
        }
        
        logger.info("消息同步触发完成，共{}批次，消息将通过回调接收", syncCount);
    }
}
