package com.weichat.api.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.initializer.InitChainManager;
import com.weichat.api.initializer.InitContext;
import com.weichat.api.strategy.CallbackStrategy;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Handle user login callbacks.
 */
@Component("userLoginStrategy")
public class UserLoginStrategy implements CallbackStrategy {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginStrategy.class);
    private static final Long INVALID_VID = 0L;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private InitChainManager initChainManager;

    @Override
    public String handle(CallbackRequest callbackRequest) {
        logger.info("处理用户登录回调，type: {}", callbackRequest.getType());

        try {
            // Keep the newest account fields returned by the login callback.
            WxUserInfo latestUserInfo = JSON.parseObject(callbackRequest.getJson(), WxUserInfo.class);
            JSONObject jsonObject = JSON.parseObject(callbackRequest.getJson());

            Long userId = getLongValue(jsonObject, "user_id", "UserId");
            Long corpId = getLongValue(jsonObject, "corp_id", "Corpid", "corpId");
            Long vid = getLongValue(jsonObject, "Vid", "vid");

            if (userId != null) {
                latestUserInfo.setUserId(userId);
            }
            if (corpId != null) {
                latestUserInfo.setCorpid(corpId);
            }
            if (vid != null) {
                latestUserInfo.setVid(vid);
            }
            latestUserInfo.setUuid(callbackRequest.getUuid());

            // Re-login is initiated from historical vid, so prefer vid matching first.
            WxUserInfo existingUser = findExistingUser(vid, userId, corpId);
            if (existingUser != null) {
                mergeLatestLoginInfo(existingUser, latestUserInfo);
                wxUserInfoService.updateByPrimaryKey(existingUser);
                logger.info("用户已存在，更新最新登录映射，vid: {}, userId: {}, corpId: {}, uuid: {}",
                        vid, userId, corpId, callbackRequest.getUuid());
            } else {
                wxUserInfoService.insert(latestUserInfo);
                logger.info("用户不存在，新增用户信息，vid: {}, userId: {}, corpId: {}, uuid: {}",
                        vid, userId, corpId, callbackRequest.getUuid());
            }

            executeInitChain(callbackRequest.getUuid(), userId, corpId);
            return "{\"success\": true, \"message\": \"用户信息处理成功\"}";
        } catch (Exception e) {
            logger.error("处理用户登录回调失败，type: {}, json: {}",
                    callbackRequest.getType(), callbackRequest.getJson(), e);
            return "{\"success\": false, \"message\": \"用户信息处理失败\"}";
        }
    }

    private WxUserInfo findExistingUser(Long vid, Long userId, Long corpId) {
        if (isValidVid(vid)) {
            WxUserInfo userByVid = wxUserInfoService.selectByVid(vid);
            if (userByVid != null) {
                return userByVid;
            }
        }
        if (userId != null && corpId != null) {
            return wxUserInfoService.selectByUserIdAndCorpId(userId, corpId);
        }
        return null;
    }

    private boolean isValidVid(Long vid) {
        return vid != null && !INVALID_VID.equals(vid);
    }

    private Long getLongValue(JSONObject jsonObject, String... keys) {
        if (jsonObject == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            Long value = jsonObject.getLong(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private void mergeLatestLoginInfo(WxUserInfo target, WxUserInfo source) {
        if (target == null || source == null) {
            return;
        }

        // Only overwrite fields that are present in the latest callback payload.
        target.setUuid(source.getUuid());
        if (source.getCorpid() != null) {
            target.setCorpid(source.getCorpid());
        }
        if (source.getUnionid() != null) {
            target.setUnionid(source.getUnionid());
        }
        if (source.getCreateTime() != null) {
            target.setCreateTime(source.getCreateTime());
        }
        if (source.getAdminVid() != null) {
            target.setAdminVid(source.getAdminVid());
        }
        if (source.getSex() != null) {
            target.setSex(source.getSex());
        }
        if (source.getMobile() != null) {
            target.setMobile(source.getMobile());
        }
        if (source.getCorpFullName() != null) {
            target.setCorpFullName(source.getCorpFullName());
        }
        if (source.getAcctid() != null) {
            target.setAcctid(source.getAcctid());
        }
        if (source.getAvatar() != null) {
            target.setAvatar(source.getAvatar());
        }
        if (source.getCorpName() != null) {
            target.setCorpName(source.getCorpName());
        }
        if (source.getEnglishName() != null) {
            target.setEnglishName(source.getEnglishName());
        }
        if (source.getRealname() != null) {
            target.setRealname(source.getRealname());
        }
        if (source.getVid() != null) {
            target.setVid(source.getVid());
        }
        if (source.getMail() != null) {
            target.setMail(source.getMail());
        }
        if (source.getOwnername() != null) {
            target.setOwnername(source.getOwnername());
        }
        if (source.getUserId() != null) {
            target.setUserId(source.getUserId());
        }
        if (source.getNickname() != null) {
            target.setNickname(source.getNickname());
        }
        if (source.getPosition() != null) {
            target.setPosition(source.getPosition());
        }
        if (source.getCorpDesc() != null) {
            target.setCorpDesc(source.getCorpDesc());
        }
        if (source.getCorpLogo() != null) {
            target.setCorpLogo(source.getCorpLogo());
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
