package com.weichat.api.vo.request.account;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 添加图片请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "添加图片请求参数")
public class AddImageRequest extends BaseRequest {

    @ApiModelProperty(value = "图片URL地址", required = true, example = "https://example.com/image.jpg")
    private String imgurl;
}
