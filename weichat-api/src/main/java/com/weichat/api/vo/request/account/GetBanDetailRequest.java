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
 * 获取封禁详情请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "获取封禁详情请求参数")
public class GetBanDetailRequest extends BaseRequest {

    @ApiModelProperty(value = "从系统消息回调中获取的封禁URL", required = true, example = "https://ban.example.com/detail")
    private String url;
}
