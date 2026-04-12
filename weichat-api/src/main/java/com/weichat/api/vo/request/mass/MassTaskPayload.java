package com.weichat.api.vo.request.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Mass task payload container")
public class MassTaskPayload {

    @ApiModelProperty(value = "Text content", example = "hello")
    private String content;

    @ApiModelProperty(value = "Media payload for image/file/voice/video")
    private MassTaskMediaPayload media;

    @ApiModelProperty(value = "Link card payload")
    private MassTaskLinkPayload link;

    @ApiModelProperty(value = "Mini program payload")
    private MassTaskAppPayload app;
}
