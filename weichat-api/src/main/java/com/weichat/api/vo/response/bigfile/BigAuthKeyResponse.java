package com.weichat.api.vo.response.bigfile;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取大文件AuthKey响应
 *
 * @author weichat
 * @since 1.0
 */
@Data
@ApiModel(description = "获取大文件AuthKey响应")
public class BigAuthKeyResponse {

    @ApiModelProperty(value = "AuthKey", example = "xxx")
    @JSONField(name = "auth_key")
    private String authKey;

    @ApiModelProperty(value = "返回码", example = "0")
    private Integer code;

    @ApiModelProperty(value = "返回消息", example = "success")
    private String msg;
}
