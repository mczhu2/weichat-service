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
 * 下载微信文件请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "下载微信文件请求参数")
public class DownloadWeChatFileRequest extends BaseRequest {

    /**
     * 微信回调里的原始下载地址，通常是 tpdownloadmedia 链接。
     */
    @JSONField(name = "url")
    @ApiModelProperty(value = "文件URL", required = true, example = "https://wx.example.com/file.jpg")
    private String fileUrl;

    /**
     * 微信资源下载鉴权 key。
     */
    @JSONField(name = "auth_key")
    @ApiModelProperty(value = "授权Key", required = true, example = "auth_key_xxx")
    private String authKey;

    /**
     * 微信资源对应的 aes_key。
     */
    @JSONField(name = "aes_key")
    @ApiModelProperty(value = "AES Key", required = true, example = "aes_key_xxx")
    private String aesKey;

    /**
     * 下载落地时使用的文件名，底层接口要求必填。
     */
    @JSONField(name = "file_name")
    @ApiModelProperty(value = "文件名", required = true, example = "image.jpg")
    private String fileName;

    /**
     * 资源大小，底层下载接口校验时会用到。
     */
    @JSONField(name = "size")
    @ApiModelProperty(value = "文件大小", required = true, example = "102400")
    private Integer size;
}
