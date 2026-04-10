package com.weichat.api.vo.response.init;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运行客户端响应（GetRunClient接口使用）
 *
 * @author weichat
 */
@Data
@ApiModel(description = "运行客户端响应数据")
public class RunClientResponse {

    @ApiModelProperty(value = "实例唯一标识", example = "uuid-xxxx-xxxx")
    private String uuid;

    @ApiModelProperty(value = "账号ID", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "账号昵称", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String headImg;

    @ApiModelProperty(value = "企业ID", example = "corp_id_xxx")
    private String corpId;

    @ApiModelProperty(value = "企业名称", example = "测试企业")
    private String corpName;

    @ApiModelProperty(value = "登录状态(0:未登录, 1:已登录)", example = "1")
    private Integer loginState;

    @ApiModelProperty(value = "在线状态", example = "1")
    private Integer onlineState;
}
