package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送引用消息请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送引用消息请求")
public class SendQuoteMsgRequest extends BaseRequest {

    @ApiModelProperty(value = "接收者ID", required = true, example = "wxid_xxx")
    private String toId;

    @ApiModelProperty(value = "回复内容", required = true, example = "收到")
    private String content;

    @ApiModelProperty(value = "被引用的消息ID", required = true, example = "12345678901234567890")
    private String quoteMsgId;

    @ApiModelProperty(value = "被引用的消息内容", example = "原消息内容")
    private String quoteContent;

    @ApiModelProperty(value = "被引用消息发送者ID", example = "wxid_yyy")
    private String quoteSenderId;
}
