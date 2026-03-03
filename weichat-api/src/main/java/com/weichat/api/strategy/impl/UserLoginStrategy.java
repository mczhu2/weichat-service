package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.initializer.InitChainManager;
import com.weichat.api.initializer.InitContext;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户登录策略
 */
@Component("userLoginStrategy")
public class UserLoginStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginStrategy.class);
    
    @Autowired
    private WxUserInfoService wxUserInfoService;
    
    @Autowired
    private InitChainManager initChainManager;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理用户登录回调，type: {}", callbackRequest.getType());
        
        try {
            // 解析JSON并自动映射到对象，支持大小写转换和下划线命名
            WxUserInfo wxUserInfo = JSON.parseObject(callbackRequest.getJson(), WxUserInfo.class);
            
            // 解析JSON获取userId和corpId，用于查询用户
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());
            Long userId = jsonObject.getLong("user_id");
            Long corpId = jsonObject.getLong("corp_id");
            wxUserInfo.setUuid(callbackRequest.getUuid());
            // 查询用户是否存在
            WxUserInfo existingUser = wxUserInfoService.selectByUserIdAndCorpId(userId, corpId);
            if (existingUser != null) {
                existingUser.setUuid(callbackRequest.getUuid());
                wxUserInfoService.updateByPrimaryKey(existingUser);
                logger.info("用户已存在，更新用户信息");
            } else {
                wxUserInfoService.insert(wxUserInfo);
                logger.info("用户不存在，新增用户信息");
            }
            
            executeInitChain(callbackRequest.getUuid(), userId, corpId);
            
            return "{\"success\": true, \"message\": \"用户信息处理成功\"}";
        } catch (Exception e) {
            logger.error("处理用户登录回调失败，type: {}, json: {}", callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"用户信息处理失败\"}";
        }
    }
    
    @Async("initExecutor")
    public void executeInitChain(String uuid, Long userId, Long corpId) {
        logger.info("开始异步执行登录后初始化，userId: {}", userId);
        try {
            InitContext context = new InitContext(uuid, userId, corpId);
            initChainManager.execute(context);
            if (context.isSuccess()) {
                logger.info("登录后初始化完成，userId: {}", userId);
            } else {
                logger.warn("登录后初始化部分失败: {}", context.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("登录后初始化异常，userId: {}", userId, e);
        }
    }
}