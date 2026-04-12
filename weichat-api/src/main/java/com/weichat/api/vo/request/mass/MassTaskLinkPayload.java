package com.weichat.api.vo.request.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Mass task link payload")
public class MassTaskLinkPayload {

    @ApiModelProperty(value = "Link URL", example = "https://www.baidu.com/")
    private String url;

    @ApiModelProperty(value = "Link title", example = "Baidu")
    private String title;

    @ApiModelProperty(value = "Link description", example = "Baidu content")
    private String content;

    @ApiModelProperty(value = "Link cover image URL", example = "https://www.baidu.com/img/xx.png")
    private String imgurl;
}
