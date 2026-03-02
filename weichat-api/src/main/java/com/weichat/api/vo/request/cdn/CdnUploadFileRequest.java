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
 * CDN上传文件请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "CDN上传文件请求参数")
public class CdnUploadFileRequest extends BaseRequest {

    @ApiModelProperty(value = "文件URL", required = true, example = "https://example.com/file.pdf")
    private String fileUrl;

    @ApiModelProperty(value = "文件名", required = true, example = "document.pdf")
    private String fileName;
}
