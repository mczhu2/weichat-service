package com.weichat.api.initializer.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.initializer.AbstractInitHandler;
import com.weichat.api.initializer.InitContext;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.enums.FriendTypeEnum;
import com.weichat.common.service.WxFriendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class FriendListInitHandler extends AbstractInitHandler {
    
    private static final int PAGE_SIZE = 100;
    
    @Autowired
    private WxWorkApiClient apiClient;
    
    @Autowired
    private WxFriendInfoService friendInfoService;
    
    @Override
    public int getOrder() {
        return 1;
    }
    
    @Override
    protected String getHandlerName() {
        return "好友列表初始化";
    }
    
    @Override
    protected void doHandle(InitContext context) {
        syncExternalContacts(context);
        syncInnerContacts(context);
    }
    
    private void syncExternalContacts(InitContext context) {
        long seq = 0;
        int totalCount = 0;
        
        while (true) {
            JSONObject result = apiClient.getExternalContacts(context.getUuid(), PAGE_SIZE, seq);
            if (result == null || result.getIntValue("errcode") != 0) {
                logger.warn("获取外部联系人失败");
                break;
            }
            
            JSONObject data = result.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            
            if (list == null || list.isEmpty()) {
                break;
            }
            
            List<WxFriendInfo> friends = parseFriendList(list, context.getUserId(), context.getCorpId());
            saveFriends(friends);
            totalCount += friends.size();
            
            seq = data.getLongValue("seq");
            if (seq == 0) {
                break;
            }
        }
        
        logger.info("同步外部联系人完成，共{}条", totalCount);
    }
    
    private void syncInnerContacts(InitContext context) {
        String strSeq = "";
        int totalCount = 0;
        
        while (true) {
            JSONObject result = apiClient.getInnerContacts(context.getUuid(), PAGE_SIZE, strSeq);
            if (result == null || result.getIntValue("errcode") != 0) {
                logger.warn("获取内部联系人失败");
                break;
            }
            
            JSONObject data = result.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            
            if (list == null || list.isEmpty()) {
                break;
            }
            
            List<WxFriendInfo> friends = parseInnerList(list, context.getUserId());
            saveFriends(friends);
            totalCount += friends.size();
            
            strSeq = data.getString("seq");
            if (strSeq == null || strSeq.isEmpty()) {
                break;
            }
        }
        
        logger.info("同步内部联系人完成，共{}条", totalCount);
    }
    
    private List<WxFriendInfo> parseFriendList(JSONArray list, Long ownerUserId, Long corpId) {
        List<WxFriendInfo> friends = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject item = list.getJSONObject(i);
            WxFriendInfo friend = new WxFriendInfo();
            friend.setOwnerUserId(ownerUserId);
            friend.setUnionid(item.getString("unionid"));
            friend.setUserId(item.getLong("user_id"));
            friend.setNickname(item.getString("nickname"));
            friend.setAvatar(item.getString("avatar"));
            friend.setSex(item.getInteger("sex"));
            friend.setMobile(item.getString("mobile"));
            friend.setRealname(item.getString("realname"));
            // 这个corpId 是顾客的，不是企业的
            friend.setCorpId(corpId);
            friend.setCreateTime(item.getLong("create_time"));
            friend.setSeq(item.getLong("seq"));
            friend.setIsExternal(FriendTypeEnum.EXTERNAL.getCode()); // 外部微信好友
            friends.add(friend);
        }
        return friends;
    }
    
    private List<WxFriendInfo> parseInnerList(JSONArray list, Long ownerUserId) {
        List<WxFriendInfo> friends = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject item = list.getJSONObject(i);
            if (item.getIntValue("is_Department") == 1) {
                continue;
            }
            WxFriendInfo friend = new WxFriendInfo();
            friend.setOwnerUserId(ownerUserId);
            friend.setUnionid(item.getString("unionid"));
            friend.setUserId(item.getLong("user_id"));
            friend.setNickname(item.getString("nickname"));
            friend.setAvatar(item.getString("avatar"));
            friend.setSex(item.getInteger("sex"));
            friend.setMobile(item.getString("mobile"));
            friend.setRealname(item.getString("realname"));
            friend.setCorpId(item.getLong("corp_id"));
            friend.setAcctid(item.getString("acctid"));
            friend.setPosition(item.getString("position"));
            friend.setIsExternal(FriendTypeEnum.ENTERPRISE.getCode()); // 企业微信好友
            friends.add(friend);
        }
        return friends;
    }
    
    private void saveFriends(List<WxFriendInfo> friends) {
        for (WxFriendInfo friend : friends) {
            WxFriendInfo existing = findExistingFriend(friend);
            if (existing != null) {
                friend.setId(existing.getId());
                friendInfoService.updateByPrimaryKey(friend);
                continue;
            }

            try {
                friendInfoService.insert(friend);
            } catch (DuplicateKeyException e) {
                // Unique key may conflict under concurrent sync or stale pre-check results.
                WxFriendInfo duplicated = findExistingFriend(friend);
                if (duplicated != null) {
                    friend.setId(duplicated.getId());
                    friendInfoService.updateByPrimaryKey(friend);
                    logger.info("好友唯一键冲突后转更新，unionid={}, ownerUserId={}", friend.getUnionid(), friend.getOwnerUserId());
                } else {
                    throw e;
                }
            }
        }
    }

    private WxFriendInfo findExistingFriend(WxFriendInfo friend) {
        return friendInfoService.selectByOwnerUserIdAndUserid(
                friend.getOwnerUserId(),
                friend.getUserId(),
                friend.getIsExternal()
        );
    }
}
