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
 * 设置代理请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "设置代理请求参数")
public class SetProxyRequest extends BaseRequest {

    @ApiModelProperty(value = "代理IP(127.0.0.1表示移除代理)", required = true, example = "192.168.1.100")
    private String ip;

    @ApiModelProperty(value = "代理端口", required = true, example = "8080")
    private Integer port;

    @ApiModelProperty(value = "代理类型(http)", required = true, example = "http")
    private String proxyType;

    @ApiModelProperty(value = "代理账号", example = "username")
    private String userName;

    @ApiModelProperty(value = "代理密码", example = "password")
    private String passward;
}
