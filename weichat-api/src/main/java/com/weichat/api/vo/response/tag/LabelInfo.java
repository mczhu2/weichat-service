package com.weichat.api.vo.response.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 标签信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "标签信息")
public class LabelInfo {

    @ApiModelProperty(value = "标签ID", example = "label_id_xxx")
    private String labelId;

    @ApiModelProperty(value = "标签名称", example = "重要客户")
    private String labelName;

    @ApiModelProperty(value = "标签颜色", example = "#FF0000")
    private String color;

    @ApiModelProperty(value = "标签下用户数量", example = "10")
    private Integer userCount;

    @ApiModelProperty(value = "创建时间(时间戳)", example = "1640000000")
    private Long createTime;
}
