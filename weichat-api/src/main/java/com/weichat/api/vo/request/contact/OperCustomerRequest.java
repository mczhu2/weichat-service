package com.weichat.api.vo.request.contact;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 操作客户请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "操作客户请求参数")
public class OperCustomerRequest extends BaseRequest {

    @ApiModelProperty(value = "客户ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "操作类型", required = true, example = "1")
    private Integer oper;
}
