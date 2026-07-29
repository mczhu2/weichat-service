package com.weichat.api.vo.request.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务系统回调路由保存请求。
 */
@Data
@ApiModel(description = "业务系统回调路由保存请求")
public class CallbackRouteUpsertRequest {

    /**
     * 企微运行实例 UUID。
     */
    @ApiModelProperty(value = "企微运行实例 UUID", required = true, example = "a4f0c2b6-xxxx")
    private String uuid;

    /**
     * 业务系统回调 URL。
     */
    @ApiModelProperty(value = "业务系统回调 URL", required = true, example = "https://customer.example.com/wecom/callback")
    private String callbackUrl;
}
