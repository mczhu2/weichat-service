package com.weichat.api.vo.response.cdn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * CDN下载响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "CDN下载响应数据")
public class CdnDownloadResponse {

    @ApiModelProperty(value = "下载URL", example = "https://cdn.example.com/download/xxx")
    private String downloadUrl;

    @ApiModelProperty(value = "文件大小(字节)", example = "102400")
    private Integer fileSize;

    @ApiModelProperty(value = "文件名", example = "document.pdf")
    private String fileName;

    @ApiModelProperty(value = "过期时间(时间戳)", example = "1640086400")
    private Long expireTime;
}
