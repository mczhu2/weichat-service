package com.weichat.api.vo.response.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送消息响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "发送消息响应数据")
public class SendMsgResponse {

    @ApiModelProperty(value = "消息ID", example = "12345")
    private Long msgId;

    @ApiModelProperty(value = "服务端消息ID", example = "server_msg_123")
    private String serverMsgId;

    @ApiModelProperty(value = "发送时间(时间戳)", example = "1640000000")
    private Long createTime;

    @ApiModelProperty(value = "发送状态(0:发送中, 1:成功, 2:失败)", example = "1")
    private Integer status;
}
