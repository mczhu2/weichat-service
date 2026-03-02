package com.weichat.api.vo.response.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 标签列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "标签列表响应数据")
public class LabelListResponse {

    @ApiModelProperty(value = "标签总数", example = "10")
    private Integer total;

    @ApiModelProperty(value = "标签列表")
    private List<LabelInfo> list;
}
