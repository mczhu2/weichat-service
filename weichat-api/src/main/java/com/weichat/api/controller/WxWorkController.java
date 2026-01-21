package com.weichat.api.controller;

import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategyFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信企业号控制器
 */
@RestController
@RequestMapping("/wxwork")
public class WxWorkController {

    private static final Logger logger = LoggerFactory.getLogger(WxWorkController.class);
    
    @Autowired
    private CallbackStrategyFactory callbackStrategyFactory;

    /**
     * 设置回调URL接口
     * @param callbackRequest 回调请求参数
     * @return 响应结果
     */
    @PostMapping(value = "/SetCallbackUrl", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> setCallbackUrl(@RequestBody CallbackRequest callbackRequest) {
        // 这里可以添加业务逻辑，比如保存回调URL配置
        logger.info("Received callback request:");
        logger.info("UUID: " + callbackRequest.getUuid());
        logger.info("JSON: " + callbackRequest.getJson());
        logger.info("Type: " + callbackRequest.getType());
        
        // 异步处理策略调用，不阻塞主线程
        handleCallbackAsync(callbackRequest);
        
        // 直接返回成功响应，不需要等待异步处理完成
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true, \"message\": \"请求已接收，正在处理\"}");
    }
    
    /**
     * 异步处理回调请求
     * @param callbackRequest 回调请求参数
     */
    @Async("taskExecutor")
    public void handleCallbackAsync(CallbackRequest callbackRequest) {
        // 保留入参中三个字段的打印日志
        logger.info("异步处理回调请求开始:");
        logger.info("UUID: " + callbackRequest.getUuid());
        logger.info("JSON: " + callbackRequest.getJson());
        logger.info("Type: " + callbackRequest.getType());
        
        String type = callbackRequest.getType();
        
        try {
            if (callbackStrategyFactory.getStrategy(type) != null) {
                // 执行对应策略
                callbackStrategyFactory.getStrategy(type).handle(callbackRequest);
            } else {
                // 未知类型，记录日志但不处理
                logger.warn("未知的回调类型: {}", type);
            }
        } catch (Exception e) {
            logger.error("处理回调请求失败，type: {}, error: {}", type, e.getMessage(), e);
        }
    }
}
