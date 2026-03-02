package com.weichat.api.vo.response.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 群发消息列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "群发消息列表响应数据")
public class GroupMsgListResponse {

    @ApiModelProperty(value = "总数", example = "10")
    private Integer total;

    @ApiModelProperty(value = "群发消息列表")
    private List<GroupMsgInfo> list;
}
