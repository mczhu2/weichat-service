package com.weichat.api.vo.request.cdn;

import com.alibaba.fastjson.annotation.JSONField;
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

    /**
     * 文件或语音的 fileid。
     */
    @JSONField(name = "fileid")
    @ApiModelProperty(value = "文件ID", required = true, example = "file_id_xxx")
    private String fileId;

    /**
     * 文件或语音资源的 aes_key。
     */
    @JSONField(name = "aes_key")
    @ApiModelProperty(value = "AES Key", required = true, example = "aes_key_xxx")
    private String aesKey;

    /**
     * 下载文件类型，语音走 5，视频走 4，图片按底层协议传 1/3。
     */
    @JSONField(name = "filetype")
    @ApiModelProperty(value = "文件类型", required = true, example = "5")
    private Integer fileType;

    /**
     * 下载落地使用的文件名。
     */
    @JSONField(name = "file_name")
    @ApiModelProperty(value = "文件名", required = true, example = "voice.silk")
    private String fileName;

    /**
     * 原始文件大小，底层下载接口会校验。
     */
    @JSONField(name = "size")
    @ApiModelProperty(value = "文件大小", required = true, example = "4096")
    private Integer size;
}
