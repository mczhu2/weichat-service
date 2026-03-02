package com.weichat.api.vo.response.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付结果响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "支付结果响应数据")
public class PaymentResultResponse {

    @ApiModelProperty(value = "支付单号", example = "pay_order_xxx")
    private String payOrderId;

    @ApiModelProperty(value = "支付状态(0:待支付, 1:支付成功, 2:支付失败)", example = "1")
    private Integer status;

    @ApiModelProperty(value = "支付金额(分)", example = "100")
    private Integer amount;

    @ApiModelProperty(value = "支付时间(时间戳)", example = "1640000000")
    private Long payTime;
}
