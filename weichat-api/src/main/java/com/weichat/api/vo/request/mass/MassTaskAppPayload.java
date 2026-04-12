package com.weichat.api.vo.request.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Mass task mini program payload")
public class MassTaskAppPayload {

    @ApiModelProperty(value = "Mini program description", example = "content")
    private String desc;

    @ApiModelProperty(value = "Mini program app name", example = "MiniProgram")
    private String appName;

    @ApiModelProperty(value = "Mini program title", example = "Mini program title")
    private String title;

    @ApiModelProperty(value = "Mini program icon URL", example = "https://www.baidu.com/img/xx.png")
    private String weappIconUrl;

    @ApiModelProperty(value = "Mini program page path", example = "pages/train/index/index.html")
    private String pagepath;

    @ApiModelProperty(value = "Mini program username", example = "gh_c4a2a98a7366@app")
    private String username;

    @ApiModelProperty(value = "Mini program appid", example = "wx45dff5234240ad90")
    private String appid;

    @ApiModelProperty(value = "Mini program cover media")
    private MassTaskMediaPayload cover;
}
