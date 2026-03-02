package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 群成员列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "群成员列表响应数据")
public class RoomMemberListResponse {

    @ApiModelProperty(value = "群ID", example = "123456789")
    private Long roomId;

    @ApiModelProperty(value = "成员总数", example = "50")
    private Integer total;

    @ApiModelProperty(value = "成员列表")
    private List<RoomMemberInfo> list;
}
