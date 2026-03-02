package com.weichat.api.vo.request.init;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 初始化请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "初始化请求参数")
public class InitRequest {

    @ApiModelProperty(value = "账号ID(首次登录可为空，登录成功后填账号ID用于自动登录)", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "代理IP", example = "127.0.0.1")
    private String ip;

    @ApiModelProperty(value = "代理端口", example = "8080")
    private String port;

    @ApiModelProperty(value = "代理类型(http)", example = "http")
    private String proxyType;

    @ApiModelProperty(value = "代理账号", example = "username")
    private String userName;

    @ApiModelProperty(value = "代理密码", example = "password")
    private String passward;

    @ApiModelProperty(value = "是否全局代理(1:全局代理, 0:非全局代理可取消)", example = "1")
    private Integer proxySituation;

    @ApiModelProperty(value = "设备类型(ipad)", example = "ipad")
    private String deverType;
}
