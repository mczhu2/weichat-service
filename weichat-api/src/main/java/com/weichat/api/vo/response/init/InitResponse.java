package com.weichat.api.vo.response.init;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 初始化响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "初始化响应数据")
public class InitResponse {

    @ApiModelProperty(value = "实例唯一标识", example = "uuid-xxxx-xxxx")
    private String uuid;

    @ApiModelProperty(value = "账号ID", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "登录状态(0:未登录, 1:已登录)", example = "1")
    private Integer loginState;

    @ApiModelProperty(value = "二维码Base64(需要扫码登录时返回)", example = "data:image/png;base64,...")
    private String qrCode;
}
