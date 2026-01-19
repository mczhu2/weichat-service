package com.weichat.api.controller;

import com.weichat.api.entity.CallbackRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * 设置回调URL接口
     * @param callbackRequest 回调请求参数
     * @return 响应结果
     */
    @PostMapping(value = "/SetCallbackUrl", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> setCallbackUrl(@RequestBody CallbackRequest callbackRequest) {
        // 这里可以添加业务逻辑，比如保存回调URL配置
        
        System.out.println("Received callback request:");
        System.out.println("UUID: " + callbackRequest.getUuid());
        System.out.println("JSON: " + callbackRequest.getJson());
        System.out.println("Type: " + callbackRequest.getType());
        
        // 返回成功响应
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true, \"message\": \"Callback URL set successfully\"}");
    }
}
