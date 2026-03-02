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
 * 添加名片好友请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "添加名片好友请求参数")
public class AddBusinessCardRequest extends BaseRequest {

    @ApiModelProperty(value = "名片用户ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "验证消息", example = "你好，我是XXX")
    private String content;
}
