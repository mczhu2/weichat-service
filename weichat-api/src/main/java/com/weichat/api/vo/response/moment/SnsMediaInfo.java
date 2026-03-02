package com.weichat.api.vo.response.moment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 朋友圈媒体信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "朋友圈媒体信息")
public class SnsMediaInfo {

    @ApiModelProperty(value = "媒体URL", example = "https://example.com/image.jpg")
    private String url;

    @ApiModelProperty(value = "缩略图URL", example = "https://example.com/thumb.jpg")
    private String thumbUrl;

    @ApiModelProperty(value = "CDN Key", example = "cdn_key_xxx")
    private String cdnKey;

    @ApiModelProperty(value = "AES Key", example = "aes_key_xxx")
    private String aesKey;

    @ApiModelProperty(value = "宽度", example = "800")
    private Integer width;

    @ApiModelProperty(value = "高度", example = "600")
    private Integer height;

    @ApiModelProperty(value = "时长(视频,秒)", example = "60")
    private Integer duration;
}
