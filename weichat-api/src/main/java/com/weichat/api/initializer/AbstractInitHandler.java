package com.weichat.api.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractInitHandler implements InitHandler {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected InitHandler next;
    
    @Override
    public void setNext(InitHandler next) {
        this.next = next;
    }
    
    @Override
    public void handle(InitContext context) {
        if (!context.isSuccess()) {
            logger.warn("上一步初始化失败，跳过: {}", getHandlerName());
            passToNext(context);
            return;
        }
        
        try {
            logger.info("开始执行初始化: {}", getHandlerName());
            doHandle(context);
            logger.info("完成初始化: {}", getHandlerName());
        } catch (Exception e) {
            logger.error("初始化失败: ", getHandlerName(), e);
            context.fail(getHandlerName() + "失败: " + e.getMessage());
        }
        
        passToNext(context);
    }
    
    protected void passToNext(InitContext context) {
        if (next != null) {
            next.handle(context);
        }
    }
    
    protected abstract void doHandle(InitContext context);
    
    protected abstract String getHandlerName();
}
