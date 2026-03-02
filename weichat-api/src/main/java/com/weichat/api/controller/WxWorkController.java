package com.weichat.api.controller;

import com.weichat.api.entity.CallbackRequest;
import com.weichat.common.entity.WxCallbackTask;
import com.weichat.common.service.WxCallbackTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "回调管理")
@RestController
@RequestMapping("/wxwork")
public class WxWorkController {

    private static final Logger logger = LoggerFactory.getLogger(WxWorkController.class);
    private static final int DEFAULT_MAX_RETRY_COUNT = 3;

    @Autowired
    private WxCallbackTaskService callbackTaskService;

    @ApiOperation("设置回调URL并存储任务")
    @PostMapping(value = "/SetCallbackUrl", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> setCallbackUrl(@RequestBody CallbackRequest callbackRequest) {
        logger.info("Received callback request, uuid: {}, type: {}",
            callbackRequest.getUuid(), callbackRequest.getType());

        WxCallbackTask task = new WxCallbackTask();
        task.setUuid(callbackRequest.getUuid());
        task.setJsonContent(callbackRequest.getJson());
        task.setType(callbackRequest.getType());
        task.setStatus(WxCallbackTask.STATUS_PENDING);
        task.setRetryCount(0);
        task.setMaxRetryCount(DEFAULT_MAX_RETRY_COUNT);

        callbackTaskService.insert(task);
        logger.info("回调任务已保存, taskId: {}", task.getId());

        return ResponseEntity.status(HttpStatus.OK)
            .body("{\"success\": true, \"message\": \"请求已接收，正在处理\"}");
    }
}
