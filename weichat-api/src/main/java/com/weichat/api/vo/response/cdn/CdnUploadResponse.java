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

    @ApiModelProperty(value = "CDN Key", example = "306b020102046430620201000204cca06f0602030f424102040bc16eb4020469a7998a042439656466633563652d343335612d343030322d396331642d3830306164666465336237650203102800020301ced0041000dd41b689a291dbe885035a5fbdcb390201010201000400")
    private String cdn_key;

    @ApiModelProperty(value = "缩略图高度", example = "387")
    private Integer thumb_image_height;

    @ApiModelProperty(value = "中图大小", example = "95287")
    private Integer mid_image_size;

    @ApiModelProperty(value = "文件大小", example = "118466")
    private Integer size;

    @ApiModelProperty(value = "缩略图宽度", example = "659")
    private Integer thumb_image_width;

    @ApiModelProperty(value = "AES Key", example = "1691E8F3D57F652AF9A26ABD26C2DB8E")
    private String aes_key;

    @ApiModelProperty(value = "宽度", example = "2196")
    private Integer width;

    @ApiModelProperty(value = "缩略图文件大小", example = "16599")
    private Integer thumb_file_size;

    @ApiModelProperty(value = "缩略图MD5", example = "7b96dd982d40484a7fb79489caca4444")
    private String thumb_file_md5;

    @ApiModelProperty(value = "文件ID", example = "306b020102046430620201000204cca06f0602030f424102040bc16eb4020469a7998a042439656466633563652d343335612d343030322d396331642d3830306164666465336237650203102800020301ced0041000dd41b689a291dbe885035a5fbdcb390201010201000400")
    private String fileid;

    @ApiModelProperty(value = "文件MD5", example = "00dd41b689a291dbe885035a5fbdcb39")
    private String md5;

    @ApiModelProperty(value = "高度", example = "1289")
    private Integer height;

    @ApiModelProperty(value = "返回码", example = "0")
    private String retcode;

    @ApiModelProperty(value = "视频时长", example = "123")
    private Integer VideoDuration;
}
