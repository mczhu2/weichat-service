package com.weichat.api.vo.request.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Mass task media payload")
public class MassTaskMediaPayload {

    @ApiModelProperty(value = "Remote media URL", example = "https://example.com/demo.jpg")
    private String url;

    @ApiModelProperty(value = "Base64 media content", example = "data:image/png;base64,...")
    private String base64;

    @ApiModelProperty(value = "Filename", example = "demo.jpg")
    private String filename;

    @ApiModelProperty(value = "Content type", example = "image/jpeg")
    private String contentType;

    @ApiModelProperty(value = "Voice duration in seconds", example = "4")
    private Integer voiceTime;

    @ApiModelProperty(value = "HD flag for image forwarding", example = "1")
    private Integer isHd;

    @ApiModelProperty(value = "Cover URL for videos", example = "https://example.com/cover.jpg")
    private String coverUrl;
}
