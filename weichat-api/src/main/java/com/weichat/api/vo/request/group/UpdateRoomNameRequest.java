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
 * 更新群名称请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "更新群名称请求参数")
public class UpdateRoomNameRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "新群名称", required = true, example = "新的群名称")
    private String name;
}
