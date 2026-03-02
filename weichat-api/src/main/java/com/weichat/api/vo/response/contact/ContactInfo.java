package com.weichat.api.vo.response.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 联系人信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "联系人信息")
public class ContactInfo {

    @ApiModelProperty(value = "用户ID", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "用户名", example = "wxid_xxx")
    private String userName;

    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "备注名", example = "张三-客户")
    private String remark;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String headImg;

    @ApiModelProperty(value = "性别(0:未知, 1:男, 2:女)", example = "1")
    private Integer sex;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String mobile;

    @ApiModelProperty(value = "邮箱", example = "test@example.com")
    private String email;

    @ApiModelProperty(value = "企业名称", example = "测试企业")
    private String corpName;

    @ApiModelProperty(value = "职位", example = "工程师")
    private String position;

    @ApiModelProperty(value = "联系人类型(1:内部, 2:外部)", example = "2")
    private Integer contactType;

    @ApiModelProperty(value = "标签ID列表")
    private List<String> labelIds;

    @ApiModelProperty(value = "描述信息", example = "重要客户")
    private String desc;
}
