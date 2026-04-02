package com.weichat.api.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 基础请求类
 * 包含所有接口通用的uuid字段
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ApiModel(description = "基础请求参数")
public class BaseRequest {

    @ApiModelProperty(value = "实例唯一标识", required = true, example = "uuid-xxxx-xxxx")
    private String uuid;
    @ApiModelProperty(value = "短信验证码", required = true, example = "254361")
    private String code;
}
