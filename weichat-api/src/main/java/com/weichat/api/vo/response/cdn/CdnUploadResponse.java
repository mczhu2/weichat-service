package com.weichat.api.vo.response.cdn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * CDN上传响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "CDN上传响应数据")
public class CdnUploadResponse {

    @ApiModelProperty(value = "CDN Key", example = "cdn_key_xxx")
    private String cdnKey;

    @ApiModelProperty(value = "AES Key", example = "aes_key_xxx")
    private String aesKey;

    @ApiModelProperty(value = "文件MD5", example = "d41d8cd98f00b204e9800998ecf8427e")
    private String md5;

    @ApiModelProperty(value = "文件大小(字节)", example = "102400")
    private Integer fileSize;

    @ApiModelProperty(value = "文件URL(部分接口返回)", example = "https://cdn.example.com/file.jpg")
    private String fileUrl;

    @ApiModelProperty(value = "宽度(图片/视频)", example = "800")
    private Integer width;

    @ApiModelProperty(value = "高度(图片/视频)", example = "600")
    private Integer height;

    @ApiModelProperty(value = "时长(音频/视频,秒)", example = "60")
    private Integer duration;
}
