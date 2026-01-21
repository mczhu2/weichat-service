package com.weichat.api.strategy;

import com.weichat.api.strategy.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 回调策略工厂
 */
@Component
public class CallbackStrategyFactory {

    private final Map<String, CallbackStrategy> strategyMap = new HashMap<>();

    /**
     * 构造函数，注入所有策略
     */
    @Autowired
    public CallbackStrategyFactory(UserLoginStrategy userLoginStrategy,
                                   MessageStrategy messageStrategy,
                                   GroupCreateStrategy groupCreateStrategy,
                                   GroupMemberAddStrategy groupMemberAddStrategy,
                                   GroupUpdateStrategy groupUpdateStrategy,
                                   FriendChangeStrategy friendChangeStrategy) {
        // 注册策略
        strategyMap.put("104001", userLoginStrategy);
        strategyMap.put("102000", messageStrategy);
        strategyMap.put("115001", groupCreateStrategy);
        strategyMap.put("115005", groupUpdateStrategy);
        strategyMap.put("115003", groupMemberAddStrategy);
        strategyMap.put("116002", friendChangeStrategy);
    }

    /**
     * 根据类型获取策略
     * @param type 类型
     * @return 策略
     */
    public CallbackStrategy getStrategy(String type) {
        return strategyMap.get(type);
    }
}