package com.weichat.api.vo.request.payment;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 发送支付请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送支付请求参数")
public class SendPaymentRequest extends BaseRequest {

    @ApiModelProperty(value = "收款人ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "金额(分)", required = true, example = "100")
    private Integer amount;
}
