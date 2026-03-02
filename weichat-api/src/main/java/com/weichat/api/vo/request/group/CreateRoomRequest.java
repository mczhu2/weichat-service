package com.weichat.api.vo.request.group;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 创建群请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "创建群请求参数")
public class CreateRoomRequest extends BaseRequest {

    @ApiModelProperty(value = "成员ID列表", required = true, example = "[111,222,333]")
    private String vids;

    @ApiModelProperty(value = "群名称", example = "我的群聊")
    private String name;
}
