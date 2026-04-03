package com.weichat.api.vo.request.init;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 检查验证码请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "检查验证码请求参数")
public class CheckCodeRequest extends BaseRequest {

    @ApiModelProperty(value = "登录二维码key", required = true, example = "B547FC8D049DFA333D89075C04BA9178")
    private String qrcodeKey;

    @ApiModelProperty(value = "短信验证码", required = true, example = "369130")
    private String code;
}
