package com.weichat.api.vo.request.init;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 设置回调URL请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "设置回调URL请求参数")
public class SetCallbackUrlRequest extends BaseRequest {

    @ApiModelProperty(value = "消息回调地址(HTTP回调)", example = "http://your-callback-url.com/callback")
    private String url;

    @ApiModelProperty(value = "回调类型(RABBITMQ)", example = "RABBITMQ")
    private String callbackType;

    @ApiModelProperty(value = "MQ交换机(direct类型)", example = "weichat.exchange")
    private String mqExchange;

    @ApiModelProperty(value = "MQ路由键", example = "weichat.routing.key")
    private String mqRoutingKey;

    @ApiModelProperty(value = "自定义回调额外内容", example = "{\"extra\":\"data\"}")
    private String extraContent;
}
