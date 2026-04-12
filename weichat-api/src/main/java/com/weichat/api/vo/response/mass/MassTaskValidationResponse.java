package com.weichat.api.vo.response.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Mass task validation result")
public class MassTaskValidationResponse {

    @ApiModelProperty(value = "Whether validation passed", example = "true")
    private Boolean valid;

    @ApiModelProperty(value = "Validation errors")
    private List<String> errors;
}
