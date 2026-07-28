package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 客户群列表响应。
 *
 * @author weichat
 */
@Data
@ApiModel(description = "客户群列表响应数据")
public class ChatroomListResponse {

    @ApiModelProperty(value = "客户群总数", example = "7")
    private Integer total;

    @ApiModelProperty(value = "下一次查询起始下标", example = "5")
    private Integer next_start;

    @ApiModelProperty(value = "客户群列表")
    private List<ChatroomInfo> roomList;
}
