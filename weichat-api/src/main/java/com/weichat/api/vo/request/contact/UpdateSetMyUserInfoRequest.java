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
 * 更新用户信息设置请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "更新用户信息设置请求参数")
public class UpdateSetMyUserInfoRequest extends BaseRequest {

    @ApiModelProperty(value = "用户ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "备注名", example = "张三")
    private String remark;

    @ApiModelProperty(value = "描述信息", example = "我的好友")
    private String desc;
}
