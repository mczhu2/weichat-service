package com.weichat.api.vo.response.chattag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会话标签列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "会话标签列表响应数据")
public class SessionTagListResponse {

    @ApiModelProperty(value = "总数", example = "5")
    private Integer total;

    @ApiModelProperty(value = "会话标签列表")
    private List<SessionTagInfo> list;
}
