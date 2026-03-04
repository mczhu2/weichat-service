package com.weichat.api.vo.request.cdn;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * CDN上传图片请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "CDN上传图片请求参数")
public class CdnUploadImgRequest extends BaseRequest {

    @ApiModelProperty(value = "图片URL", required = true, example = "https://example.com/image.jpg")
    private String url;
}
