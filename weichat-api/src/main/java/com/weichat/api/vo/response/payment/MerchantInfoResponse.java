package com.weichat.api.vo.response.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商户信息响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "商户信息响应数据")
public class MerchantInfoResponse {

    @ApiModelProperty(value = "商户ID", example = "mch_id_xxx")
    private String mchId;

    @ApiModelProperty(value = "商户名称", example = "测试商户")
    private String mchName;

    @ApiModelProperty(value = "商户状态(0:未开通, 1:已开通)", example = "1")
    private Integer status;

    @ApiModelProperty(value = "可用余额(分)", example = "10000")
    private Long balance;
}
