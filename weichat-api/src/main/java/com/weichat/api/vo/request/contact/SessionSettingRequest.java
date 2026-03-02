package com.weichat.api.vo.request.contact;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 设置会话状态请求(置顶/免打扰/折叠/标记)
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "设置会话状态请求参数")
public class SessionSettingRequest extends BaseRequest {

    @ApiModelProperty(value = "会话ID(好友或群ID)", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "是否置顶(true/false)", example = "true")
    private Boolean isTop;

    @ApiModelProperty(value = "是否免打扰(true/false)", example = "false")
    private Boolean isShield;

    @ApiModelProperty(value = "是否折叠(true/false)", example = "false")
    private Boolean isFold;

    @ApiModelProperty(value = "是否标记(true/false)", example = "true")
    private Boolean isMark;
}
