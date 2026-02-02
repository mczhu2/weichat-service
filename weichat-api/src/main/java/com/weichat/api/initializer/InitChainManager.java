package com.weichat.api.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

@Component
public class InitChainManager {
    
    private static final Logger logger = LoggerFactory.getLogger(InitChainManager.class);
    
    @Autowired
    private List<InitHandler> handlers;
    
    private InitHandler head;
    
    @PostConstruct
    public void init() {
        if (handlers == null || handlers.isEmpty()) {
            return;
        }
        
        handlers.sort(Comparator.comparingInt(InitHandler::getOrder));
        
        head = handlers.get(0);
        for (int i = 0; i < handlers.size() - 1; i++) {
            handlers.get(i).setNext(handlers.get(i + 1));
        }
        
        logger.info("初始化链构建完成，共{}个处理器", handlers.size());
    }
    
    public void execute(InitContext context) {
        if (head != null) {
            head.handle(context);
        }
    }
}
