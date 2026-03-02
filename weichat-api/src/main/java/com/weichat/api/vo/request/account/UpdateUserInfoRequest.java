package com.weichat.api.vo.request.account;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 更新用户信息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "更新用户信息请求参数")
public class UpdateUserInfoRequest extends BaseRequest {

    @ApiModelProperty(value = "邮箱", example = "test@example.com")
    private String emile;

    @ApiModelProperty(value = "名字", example = "张三")
    private String name;

    @ApiModelProperty(value = "英文名", example = "Tom")
    private String english_name;

    @ApiModelProperty(value = "座机", example = "010-12345678")
    private String phone;

    @ApiModelProperty(value = "职位", example = "工程师")
    private String position;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String mobile;

    @ApiModelProperty(value = "性别(1:男, 2:女)", example = "1")
    private Integer gender;
}
