package com.weichat.api.initializer.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.initializer.AbstractInitHandler;
import com.weichat.api.initializer.InitContext;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.service.WxGroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class GroupListInitHandler extends AbstractInitHandler {
    
    private static final int PAGE_SIZE = 50;
    
    @Autowired
    private WxWorkApiClient apiClient;
    
    @Autowired
    private WxGroupInfoService groupInfoService;
    
    @Override
    public int getOrder() {
        return 2;
    }
    
    @Override
    protected String getHandlerName() {
        return "群列表初始化";
    }
    
    @Override
    protected void doHandle(InitContext context) {
        int startIndex = 0;
        int totalCount = 0;
        
        while (true) {
            JSONObject result = apiClient.getChatroomList(
                context.getUuid(), PAGE_SIZE, startIndex);
            
            if (result == null || result.getIntValue("errcode") != 0) {
                logger.warn("获取群列表失败");
                break;
            }
            
            JSONObject data = result.getJSONObject("data");
            JSONArray roomList = data.getJSONArray("roomList");
            
            if (roomList == null || roomList.isEmpty()) {
                break;
            }
            
            for (int i = 0; i < roomList.size(); i++) {
                JSONObject item = roomList.getJSONObject(i);
                saveGroupInfo(item);
                totalCount++;
            }
            
            int nextStart = data.getIntValue("next_start");
            if (nextStart <= startIndex) {
                break;
            }
            startIndex = nextStart;
        }
        
        logger.info("同步群列表完成，共{}个群", totalCount);
    }
    
    private void saveGroupInfo(JSONObject item) {
        String roomId = item.getString("room_id");
        WxGroupInfo group = new WxGroupInfo();
        group.setRoomId(roomId);
        group.setNickname(item.getString("nickname"));
        group.setTotal(item.getInteger("total"));
        group.setCreateTime(item.getLong("create_time"));
        group.setCreateUserId(item.getLong("create_user_id"));
        
        WxGroupInfo existing = groupInfoService.selectByRoomId(roomId);
        if (existing != null) {
            group.setId(existing.getId());
            groupInfoService.updateByPrimaryKey(group);
        } else {
            groupInfoService.insert(group);
        }
    }
}
