package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 发送视频消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送视频消息请求参数")
public class SendVideoRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群ID", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "是否群消息", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "CDN Key", required = true, example = "cdn_key_xxx")
    private String cdnkey;

    @ApiModelProperty(value = "AES Key", required = true, example = "aes_key_xxx")
    private String aeskey;

    @ApiModelProperty(value = "文件MD5", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    private String md5;

    @ApiModelProperty(value = "视频时长(秒)", required = true, example = "60")
    private Integer video_duration;

    @ApiModelProperty(value = "视频宽度", example = "1920")
    private Integer video_width;

    @ApiModelProperty(value = "视频高度", example = "1080")
    private Integer video_height;

    @ApiModelProperty(value = "文件大小", required = true, example = "10240000")
    private Integer fileSize;
}
