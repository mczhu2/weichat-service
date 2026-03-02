package com.weichat.api.vo.response.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 客服列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "客服列表响应数据")
public class KfListResponse {

    @ApiModelProperty(value = "总数", example = "5")
    private Integer total;

    @ApiModelProperty(value = "客服列表")
    private List<KfInfo> list;
}
