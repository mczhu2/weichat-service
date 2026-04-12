package com.weichat.api.vo.response.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Mass message field spec")
public class MassMessageFieldSpecResponse {

    @ApiModelProperty(value = "Field path", example = "payload.content")
    private String field;

    @ApiModelProperty(value = "Field label", example = "Content")
    private String label;

    @ApiModelProperty(value = "Field description", example = "Required for text message")
    private String description;

    @ApiModelProperty(value = "Field type", example = "string")
    private String fieldType;

    @ApiModelProperty(value = "Whether the field is required", example = "true")
    private Boolean required;
}
