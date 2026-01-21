package com.weichat.api.strategy;

import com.weichat.api.entity.CallbackRequest;

/**
 * 回调策略接口
 */
public interface CallbackStrategy {
    /**
     * 处理回调请求
     * @param callbackRequest 回调请求
     * @return 处理结果
     */
    String handle(CallbackRequest callbackRequest);
}