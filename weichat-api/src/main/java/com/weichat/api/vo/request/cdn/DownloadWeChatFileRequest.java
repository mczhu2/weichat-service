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

    @ApiModelProperty(value = "文件URL", required = true, example = "https://wx.example.com/file.jpg")
    private String fileUrl;

    @ApiModelProperty(value = "授权Key", required = true, example = "auth_key_xxx")
    private String authKey;
}
