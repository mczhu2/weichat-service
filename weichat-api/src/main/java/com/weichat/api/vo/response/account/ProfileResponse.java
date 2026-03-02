package com.weichat.api.vo.response.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号信息响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "账号信息响应数据")
public class ProfileResponse {

    @ApiModelProperty(value = "账号ID", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "用户名", example = "wxid_xxx")
    private String userName;

    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String headImg;

    @ApiModelProperty(value = "性别(0:未知, 1:男, 2:女)", example = "1")
    private Integer sex;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String mobile;

    @ApiModelProperty(value = "邮箱", example = "test@example.com")
    private String email;

    @ApiModelProperty(value = "英文名", example = "Tom")
    private String englishName;

    @ApiModelProperty(value = "职位", example = "工程师")
    private String position;

    @ApiModelProperty(value = "座机", example = "010-12345678")
    private String phone;

    @ApiModelProperty(value = "企业ID", example = "123456")
    private Long corpId;

    @ApiModelProperty(value = "企业名称", example = "测试企业")
    private String corpName;

    @ApiModelProperty(value = "部门名称", example = "技术部")
    private String department;

    @ApiModelProperty(value = "自动同意设置(0:自动同意, 1:需要验证)", example = "1")
    private Integer autoAgree;
}
