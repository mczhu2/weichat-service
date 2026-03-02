package com.weichat.api.vo.request.service;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 接入客户请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "接入客户请求参数")
public class ConnectWithCustomersRequest extends BaseRequest {

    @ApiModelProperty(value = "客服ID", required = true, example = "kf_id_xxx")
    private String kfId;

    @ApiModelProperty(value = "客户ID", required = true, example = "customer_id_xxx")
    private String customerId;
}
