package com.weichat.api.initializer.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.initializer.AbstractInitHandler;
import com.weichat.api.initializer.InitContext;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxGroupMember;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(3)
public class GroupMemberInitHandler extends AbstractInitHandler {
    
    @Autowired
    private WxWorkApiClient apiClient;
    
    @Autowired
    private WxGroupInfoService groupInfoService;
    
    @Autowired
    private WxGroupMemberService groupMemberService;
    
    @Override
    public int getOrder() {
        return 3;
    }
    
    @Override
    protected String getHandlerName() {
        return "群成员初始化";
    }
    
    @Override
    protected void doHandle(InitContext context) {
        List<WxGroupInfo> groups = groupInfoService.selectAll();
        int totalMembers = 0;
        
        for (WxGroupInfo group : groups) {
            int count = syncGroupMembers(context.getUuid(), group.getRoomId());
            totalMembers += count;
        }
        
        logger.info("同步群成员完成，共{}人", totalMembers);
    }
    
    private int syncGroupMembers(String uuid, String roomId) {
        JSONObject result = apiClient.getRoomUserList(uuid, Long.parseLong(roomId));
        
        if (result == null || result.getIntValue("errcode") != 0) {
            logger.warn("获取群成员失败, roomId: {}", roomId);
            return 0;
        }
        
        JSONObject data = result.getJSONObject("data");
        JSONArray memberList = data.getJSONArray("member_list");
        
        if (memberList == null || memberList.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        for (int i = 0; i < memberList.size(); i++) {
            JSONObject item = memberList.getJSONObject(i);
            saveMember(roomId, item);
            count++;
        }
        
        return count;
    }
    
    private void saveMember(String roomId, JSONObject item) {
        WxGroupMember member = new WxGroupMember();
        member.setRoomId(roomId);
        member.setUnionid(item.getString("unionid"));
        member.setUin(item.getLong("uin"));
        member.setNickname(item.getString("nickname"));
        member.setRoomNickname(item.getString("room_nickname"));
        member.setAvatar(item.getString("avatar"));
        member.setSex(item.getInteger("sex"));
        member.setJointime(item.getLong("jointime"));
        member.setJoinScene(item.getInteger("join_scene"));
        member.setInviteUserId(item.getLong("invite_user_id"));
        member.setCorpId(item.getLong("corp_id"));
        
        WxGroupMember existing = groupMemberService.selectByRoomIdAndUin(
            roomId, member.getUin());
        if (existing != null) {
            member.setId(existing.getId());
            groupMemberService.updateByPrimaryKey(member);
        } else {
            groupMemberService.insert(member);
        }
    }
}
