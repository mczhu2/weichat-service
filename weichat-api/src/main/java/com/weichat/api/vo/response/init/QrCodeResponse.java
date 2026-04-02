package com.weichat.api.vo.response.init;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 二维码响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "二维码响应数据")
public class QrCodeResponse {

    @ApiModelProperty(value = "二维码Base64编码", example = "data:image/png;base64,iVBORw0KGgo...")
    private String qrCode;

    @ApiModelProperty(value = "二维码Key-后续验证码需要附带填写", example = "78D0489F92F7DDFE2FD73CD266473248")
    private String Key;

    @ApiModelProperty(value = "二维码状态(0:等待扫码, 1:已扫码待确认, 2:已确认)", example = "0")
    private Integer status;

    @ApiModelProperty(value = "过期时间(秒)", example = "300")
    private Integer expireTime;
}
