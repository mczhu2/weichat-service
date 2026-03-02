package com.weichat.api.vo.response.chattag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会话标签信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "会话标签信息")
public class SessionTagInfo {

    @ApiModelProperty(value = "标签ID", example = "tag_id_xxx")
    private String tagId;

    @ApiModelProperty(value = "标签名称", example = "重要会话")
    private String tagName;

    @ApiModelProperty(value = "标签颜色", example = "#FF0000")
    private String color;

    @ApiModelProperty(value = "会话数量", example = "10")
    private Integer sessionCount;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;
}
