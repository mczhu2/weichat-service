package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建群响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "创建群响应数据")
public class CreateRoomResponse {

    @ApiModelProperty(value = "群ID", example = "123456789")
    private Long roomId;

    @ApiModelProperty(value = "群名称", example = "新建群聊")
    private String name;
}
