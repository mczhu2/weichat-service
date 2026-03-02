package com.weichat.api.vo.response.moment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 朋友圈链接信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "朋友圈链接信息")
public class SnsLinkInfo {

    @ApiModelProperty(value = "链接URL", example = "https://example.com/article")
    private String url;

    @ApiModelProperty(value = "链接标题", example = "文章标题")
    private String title;

    @ApiModelProperty(value = "链接描述", example = "文章描述")
    private String desc;

    @ApiModelProperty(value = "链接封面图", example = "https://example.com/cover.jpg")
    private String coverUrl;
}
