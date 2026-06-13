package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.CustomerReplyService;
import com.weichat.api.vo.request.message.WecomMiniAppReplyRequest;
import com.weichat.api.vo.response.message.SendMsgResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "WeCom reply")
@RestController
@RequestMapping("/api/v1/wecom/reply")
public class WecomReplyController {

    private static final Logger logger = LoggerFactory.getLogger(WecomReplyController.class);

    @Autowired
    private CustomerReplyService customerReplyService;

    @ApiOperation("Send mini app reply from WeCom account to customer")
    @PostMapping("/miniApp")
    public ApiResult<SendMsgResponse> sendMiniAppReply(@RequestBody WecomMiniAppReplyRequest request) {
        logger.info(
                "Received mini app reply request. uuid={}, replySender={}, replyReceiver={}, replyAccountUserId={}, isRoom={}, roomId={}, hasKfId={}, title={}, appid={}, username={}, pagepath={}",
                request == null ? null : request.getUuid(),
                request == null ? null : request.getReplySender(),
                request == null ? null : request.getReplyReceiver(),
                request == null ? null : request.getReplyAccountUserId(),
                request == null ? null : request.getIsRoom(),
                request == null ? null : request.getRoomId(),
                request != null && request.getKfId() != null,
                request == null ? null : request.getTitle(),
                request == null ? null : request.getAppid(),
                request == null ? null : request.getUsername(),
                request == null ? null : request.getPagepath()
        );
        ApiResult<SendMsgResponse> result = customerReplyService.sendMiniAppReply(request);
        logger.info(
                "Mini app reply request finished. uuid={}, replyReceiver={}, isRoom={}, code={}, msg={}",
                request == null ? null : request.getUuid(),
                request == null ? null : request.getReplyReceiver(),
                request == null ? null : request.getIsRoom(),
                result == null ? null : result.getCode(),
                result == null ? null : result.getMsg()
        );
        return result;
    }
}
