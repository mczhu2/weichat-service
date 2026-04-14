package com.weichat.api.vo.response.bigfile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片尺寸信息
 *
 * @author weichat
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "图片尺寸信息")
public class ImageDimension {

    @ApiModelProperty(value = "图片宽度（像素）", example = "1920")
    private int width;

    @ApiModelProperty(value = "图片高度（像素）", example = "1080")
    private int height;
}
