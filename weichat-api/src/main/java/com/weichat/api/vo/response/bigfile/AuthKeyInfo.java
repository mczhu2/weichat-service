package com.weichat.api.vo.response.bigfile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthKey信息
 *
 * @author weichat
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "AuthKey信息")
public class AuthKeyInfo {

    @ApiModelProperty(value = "AuthKey", example = "xxx")
    private String authKey;

    @ApiModelProperty(value = "FileKey", example = "xxx")
    private String fileKey;
}
