package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.config.ApiConfig;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxGroupMember;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxGroupMemberService;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 群成员新增策略
 */
@Component("groupMemberAddStrategy")
public class GroupMemberAddStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GroupMemberAddStrategy.class);
    
    @Autowired
    private WxGroupMemberService wxGroupMemberService;
    
    @Autowired
    private WxUserInfoService wxUserInfoService;
    
    @Autowired
    private ApiConfig apiConfig;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理群成员新增回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());
            
            // 提取群ID，注意字段是room_conversation_id
            String roomId = jsonObject.getString("room_conversation_id");

            // 提取member_list中的userId
            JSONArray memberList = jsonObject.getJSONArray("member_list");
            // 提取invitee作为邀请者ID
            Long inviteUserId = jsonObject.getLong("invitee");
            
            // 如果member_list不为空，则进行处理
            if (memberList != null && !memberList.isEmpty()) {
                // 将member_list转换为字符串数组 for API call
                List<String> vids = new ArrayList<>();
                for (Object memberObj : memberList) {
                    String vid = memberObj instanceof Number ? 
                        String.valueOf(((Number) memberObj).longValue()) : 
                        memberObj.toString();
                    vids.add(vid);
                }
                
                // 调用外部API获取用户详细信息
                List<WxUserInfo> userInfoList = getUserDetailsByVids(vids, callbackRequest.getUuid());
                
                // 遍历userInfoList，处理每个新增的群成员
                for (int i = 0; i < userInfoList.size(); i++) {
                    WxUserInfo userInfo = userInfoList.get(i);
                    
                    if (userInfo != null) {
                        // 获取uin值 from the user info
                        Long uin = userInfo.getUserId();
                        
                        // 查询是否已存在
                        WxGroupMember existingMember = wxGroupMemberService.selectByRoomIdAndUin(roomId, uin);
                        
                        if (existingMember == null) {
                            // 不存在则创建新群成员对象并插入
                            WxGroupMember groupMember = new WxGroupMember();
                            groupMember.setRoomId(roomId);
                            groupMember.setUin(uin);
                            groupMember.setInviteUserId(inviteUserId);
                            groupMember.setCreateTime(System.currentTimeMillis());
                            groupMember.setJointime(System.currentTimeMillis());
                            
                            wxGroupMemberService.insert(groupMember);
                            logger.info("群成员新增处理成功，群ID: {}, 用户ID: {}", roomId, uin);
                        } else {
                            // 存在则更新邀请者ID
                            existingMember.setInviteUserId(inviteUserId);
                            existingMember.setJointime(System.currentTimeMillis()); // 更新加入时间
                            wxGroupMemberService.updateByPrimaryKey(existingMember);
                            logger.info("群成员邀请者更新成功，群ID: {}, 用户ID: {}", roomId, uin);
                        }
                        
                        // 设置UUID
                        userInfo.setUuid(callbackRequest.getUuid());
                        
                        // 检查数据库中是否已存在该用户
                        WxUserInfo existingUserInfo = wxUserInfoService.selectByUserIdAndCorpId(userInfo.getUserId(), userInfo.getCorpid());
                        if (existingUserInfo != null) {
                            // 更新现有用户信息
                            userInfo.setId(existingUserInfo.getId());
                            wxUserInfoService.updateByPrimaryKey(userInfo);
                            logger.info("用户信息更新成功，用户ID: {}", userInfo.getUserId());
                        } else {
                            // 插入新用户信息
                            wxUserInfoService.insert(userInfo);
                            logger.info("用户信息新增成功，用户ID: {}", userInfo.getUserId());
                        }
                    }
                }
            } else {
                // 如果没有member_list，可能需要记录日志或采取其他措施
                logger.warn("群成员新增回调中未找到有效的member_list，callback: {}", callbackRequest.getJson());
            }
            
            return "{\"success\": true, \"message\": \"群成员新增处理成功\"}";
        } catch (Exception e) {
            logger.error("处理群成员新增回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"群成员新增处理失败\"}";
        }
    }
    
    /**
     * 根据VID列表获取用户详细信息
     * @param vids VID列表
     * @param uuid UUID
     * @return 用户信息列表
     */
    private List<WxUserInfo> getUserDetailsByVids(List<String> vids, String uuid) {
        try {
            // 创建RestTemplate实例
            RestTemplate restTemplate = new RestTemplate();
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("uuid", uuid);
            requestBody.put("vids", vids);
            
            // 创建HTTP请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toJSONString(), headers);
            
            // 发送POST请求 using configured API base URL
            String url = apiConfig.getApiBaseUrl() + "/wxwork/GetUserInfoByVids";
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            
        // 解析响应
            String responseBody = responseEntity.getBody();
            if (responseBody != null) {
                JSONObject responseJson = JSON.parseObject(responseBody);
                
                // 检查错误码
                int errCode = responseJson.getIntValue("errcode");
                if (errCode == 0) {
                    // 成功获取用户信息
                    JSONArray dataArray = responseJson.getJSONArray("data");
                    if (dataArray != null && !dataArray.isEmpty()) {
                        List<WxUserInfo> userInfoList = new ArrayList<>();
                        
                        for (Object obj : dataArray) {
                            JSONObject userJson = (JSONObject) obj;
                            
                            // 创建WxUserInfo对象并设置属性
                            WxUserInfo userInfo = new WxUserInfo();
                            userInfo.setUnionid(userJson.getString("unionid"));
                            userInfo.setCreateTime(userJson.getLong("create_time"));
                            userInfo.setUserId(userJson.getLong("user_id"));
                            userInfo.setSex(userJson.getInteger("sex"));
                            userInfo.setMobile(userJson.getString("mobile"));
                            userInfo.setNickname(userJson.getString("nickname"));
                            userInfo.setAcctid(userJson.getString("acctid"));
                            userInfo.setAvatar(userJson.getString("avatar"));
                            userInfo.setPosition(userJson.getString("position"));
                            userInfo.setCorpid(userJson.getLong("corp_id"));
                            userInfo.setEnglishName(userJson.getString("english_name"));
                            userInfo.setRealname(userJson.getString("realname"));
                            
                            userInfoList.add(userInfo);
                        }
                        
                        return userInfoList;
                    }
                } else {
                    logger.error("获取用户信息失败，错误码: {}, 错误信息: {}", errCode, responseJson.getString("errmsg"));
                }
            }
        } catch (Exception e) {
            logger.error("调用获取用户信息接口失败", e);
        }
        
        return new ArrayList<>(); // 返回空列表
    }
}