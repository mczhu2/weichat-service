package com.weichat.api.vo.response.bigfile;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 大文件上传响应
 *
 * @author weichat
 * @since 1.0
 */
@Data
@ApiModel(description = "大文件上传响应")
public class BigFileUploadResponse {

    @ApiModelProperty(value = "文件ID", example = "xxx")
    private String fileid;

    @ApiModelProperty(value = "AES Key", example = "xxx")
    @JSONField(name = "aes_key")
    private String aesKey;

    @ApiModelProperty(value = "文件MD5", example = "xxx")
    private String md5;

    @ApiModelProperty(value = "文件大小（字节）", example = "1024000")
    private Integer size;

    @ApiModelProperty(value = "图片宽度（像素）", example = "1920")
    private Integer width;

    @ApiModelProperty(value = "图片高度（像素）", example = "1080")
    private Integer height;

    @ApiModelProperty(value = "视频时长（秒）", example = "60")
    @JSONField(name = "video_len")
    private Integer videoLen;

    @ApiModelProperty(value = "返回码", example = "0")
    private Integer code;

    @ApiModelProperty(value = "返回消息", example = "success")
    private String msg;
}
