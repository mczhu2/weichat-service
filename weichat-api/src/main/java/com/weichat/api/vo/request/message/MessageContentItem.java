package com.weichat.api.vo.request.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 消息内容项
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ApiModel(description = "消息内容项")
public class MessageContentItem {

    @ApiModelProperty(value = "消息类型(0:文本, 3:表情)", required = true, example = "0")
    private Integer msgtype;

    @ApiModelProperty(value = "消息内容", required = true, example = "你好")
    private String msg;
}
