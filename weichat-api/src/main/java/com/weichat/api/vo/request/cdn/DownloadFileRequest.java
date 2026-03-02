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
 * 下载文件请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "下载文件请求参数")
public class DownloadFileRequest extends BaseRequest {

    @ApiModelProperty(value = "CDN Key", required = true, example = "cdn_key_xxx")
    private String cdnkey;

    @ApiModelProperty(value = "AES Key", required = true, example = "aes_key_xxx")
    private String aeskey;
}
