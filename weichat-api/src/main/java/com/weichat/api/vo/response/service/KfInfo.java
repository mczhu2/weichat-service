package com.weichat.api.vo.response.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客服信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "客服信息")
public class KfInfo {

    @ApiModelProperty(value = "客服ID", example = "kf_id_xxx")
    private String kfId;

    @ApiModelProperty(value = "客服名称", example = "客服小张")
    private String kfName;

    @ApiModelProperty(value = "客服头像", example = "https://example.com/avatar.jpg")
    private String kfAvatar;

    @ApiModelProperty(value = "客服状态(0:离线, 1:在线, 2:忙碌)", example = "1")
    private Integer state;

    @ApiModelProperty(value = "接待中客户数", example = "5")
    private Integer servingCount;

    @ApiModelProperty(value = "最大接待数", example = "10")
    private Integer maxServingCount;
}
