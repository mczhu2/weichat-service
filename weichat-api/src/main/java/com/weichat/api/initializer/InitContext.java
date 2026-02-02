package com.weichat.api.initializer;

import lombok.Data;

@Data
public class InitContext {
    
    private String uuid;
    
    private Long userId;
    
    private Long corpId;
    
    private boolean success = true;
    
    private String errorMessage;
    
    public InitContext(String uuid, Long userId, Long corpId) {
        this.uuid = uuid;
        this.userId = userId;
        this.corpId = corpId;
    }
    
    public void fail(String message) {
        this.success = false;
        this.errorMessage = message;
    }
}
