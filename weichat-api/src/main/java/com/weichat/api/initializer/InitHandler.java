package com.weichat.api.initializer;

public interface InitHandler {
    
    void setNext(InitHandler next);
    
    void handle(InitContext context);
    
    int getOrder();
}
