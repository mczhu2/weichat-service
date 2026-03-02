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
 * 群设置开关请求(禁止修改群名/邀请确认/反垃圾)
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "群设置开关请求参数")
public class RoomSettingRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "是否禁止(true/false)", example = "true")
    private Boolean disable;

    @ApiModelProperty(value = "是否需要确认(true/false)", example = "true")
    private Boolean confirm;

    @ApiModelProperty(value = "是否启用(true/false)", example = "true")
    private Boolean enable;
}
