package com.weichat.api.vo.response.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Mass message type spec")
public class MassMessageTypeSpecResponse {

    @ApiModelProperty(value = "Message type", example = "0")
    private Integer msgType;

    @ApiModelProperty(value = "Type code", example = "text")
    private String code;

    @ApiModelProperty(value = "Type label", example = "Text")
    private String label;

    @ApiModelProperty(value = "Whether current task table can store this type", example = "true")
    private Boolean storageSupported;

    @ApiModelProperty(value = "Whether current task execution supports this type", example = "true")
    private Boolean executionSupported;

    @ApiModelProperty(value = "Required fields")
    private List<MassMessageFieldSpecResponse> requiredFields;

    @ApiModelProperty(value = "Optional fields")
    private List<MassMessageFieldSpecResponse> optionalFields;

    @ApiModelProperty(value = "Notes")
    private List<String> notes;
}
