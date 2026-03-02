package com.weichat.api.vo.request.account;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 自动同意设置请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "自动同意设置请求参数")
public class AutomaticConsentRequest extends BaseRequest {

    @ApiModelProperty(value = "状态(1:需要验证同意, 0:自动同意)", required = true, example = "1")
    private Integer state;
}
