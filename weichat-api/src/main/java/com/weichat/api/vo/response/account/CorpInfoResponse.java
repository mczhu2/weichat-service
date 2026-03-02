package com.weichat.api.vo.response.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业信息响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "企业信息响应数据")
public class CorpInfoResponse {

    @ApiModelProperty(value = "企业ID", example = "123456")
    private Long corpId;

    @ApiModelProperty(value = "企业名称", example = "测试企业")
    private String corpName;

    @ApiModelProperty(value = "企业Logo", example = "https://example.com/logo.png")
    private String corpLogo;

    @ApiModelProperty(value = "企业规模", example = "100-500人")
    private String corpScale;

    @ApiModelProperty(value = "企业行业", example = "互联网")
    private String corpIndustry;

    @ApiModelProperty(value = "企业地址", example = "北京市朝阳区")
    private String corpAddress;
}
