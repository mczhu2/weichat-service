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
        return "group list init";
    }

    @Override
    protected void doHandle(InitContext context) {
        int startIndex = 0;
        int totalCount = 0;

        while (true) {
            JSONObject result = apiClient.getChatroomList(context.getUuid(), PAGE_SIZE, startIndex);
            if (result == null || result.getIntValue("errcode") != 0) {
                logger.warn("failed to load group list");
                break;
            }

            JSONObject data = result.getJSONObject("data");
            JSONArray roomList = data.getJSONArray("roomList");
            if (roomList == null || roomList.isEmpty()) {
                break;
            }

            for (int i = 0; i < roomList.size(); i++) {
                saveGroupInfo(context, roomList.getJSONObject(i));
                totalCount++;
            }

            int nextStart = data.getIntValue("next_start");
            if (nextStart <= startIndex) {
                break;
            }
            startIndex = nextStart;
        }

        logger.info("group list init completed, total={}", totalCount);
    }

    private void saveGroupInfo(InitContext context, JSONObject item) {
        if (context.getCorpId() == null) {
            logger.warn("skip group save because corpId is missing, roomId={}", item.getString("room_id"));
            return;
        }

        String roomId = item.getString("room_id");
        WxGroupInfo group = new WxGroupInfo();
        group.setRoomId(roomId);
        group.setCorpId(context.getCorpId());
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
