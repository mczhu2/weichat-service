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
 * 搜索添加好友请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "搜索添加好友请求参数")
public class AddSearchRequest extends BaseRequest {

    @ApiModelProperty(value = "搜索关键词(手机号/微信号)", required = true, example = "13800138000")
    private String keyword;

    @ApiModelProperty(value = "验证消息", example = "你好，我是XXX")
    private String content;
}
