package com.weichat.api.vo.response.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 业务系统回调路由保存响应。
 */
@Data
@Builder
@ApiModel(description = "业务系统回调路由保存响应")
public class CallbackRouteUpsertResponse {

    /**
     * 企微运行实例 UUID。
     */
    @ApiModelProperty(value = "企微运行实例 UUID", example = "a4f0c2b6-xxxx")
    private String uuid;

    /**
     * 业务系统回调 URL。
     */
    @ApiModelProperty(value = "业务系统回调 URL", example = "https://customer.example.com/wecom/callback")
    private String callbackUrl;

    /**
     * 系统固定的企微平台回调入口。
     */
    @ApiModelProperty(value = "系统固定的企微平台回调入口", example = "http://ai-agent.okgcc.cn/wx/wxwork/SetCallbackUrl")
    private String platformCallbackUrl;

    /**
     * 是否已完成企微平台回调入口注册。
     */
    @ApiModelProperty(value = "是否已完成企微平台回调入口注册", example = "true")
    private Boolean platformCallbackRegistered;
}
