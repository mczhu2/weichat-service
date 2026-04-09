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
public class ThisQrCodeResponse {

    @ApiModelProperty(value = "二维码Base64编码", example = "data:image/png;base64,iVBORw0KGgo...")
    private String QrCodeUrl;

}
