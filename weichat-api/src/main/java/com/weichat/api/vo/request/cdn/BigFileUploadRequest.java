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
 * 大文件上传请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "大文件上传请求参数")
public class BigFileUploadRequest extends BaseRequest {

    @ApiModelProperty(value = "文件URL", required = true, example = "https://example.com/file.zip")
    private String fileUrl;

    @ApiModelProperty(value = "文件名", required = true, example = "document.zip")
    private String fileName;
}
