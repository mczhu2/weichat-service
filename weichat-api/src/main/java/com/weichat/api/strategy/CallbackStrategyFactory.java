package com.weichat.api.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 回调策略工厂
 */
@Component
public class CallbackStrategyFactory {

    private final Map<String, CallbackStrategy> strategyMap = new HashMap<>();

    @Autowired
    public CallbackStrategyFactory(
            @Qualifier("userLoginStrategy") CallbackStrategy userLoginStrategy,
            @Qualifier("messageStrategy") CallbackStrategy messageStrategy,
            @Qualifier("groupCreateStrategy") CallbackStrategy groupCreateStrategy,
            @Qualifier("groupMemberAddStrategy") CallbackStrategy groupMemberAddStrategy,
            @Qualifier("groupUpdateStrategy") CallbackStrategy groupUpdateStrategy,
            @Qualifier("friendChangeStrategy") CallbackStrategy friendChangeStrategy) {
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