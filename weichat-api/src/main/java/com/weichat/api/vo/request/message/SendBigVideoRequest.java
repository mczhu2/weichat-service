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
 * 发送大视频消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送大视频消息请求参数")
public class SendBigVideoRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群ID", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "是否群消息", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "CDN Key(大文件上传获取)", required = true, example = "cdn_key_xxx")
    private String cdnkey;

    @ApiModelProperty(value = "文件名", required = true, example = "video.mp4")
    private String file_name;

    @ApiModelProperty(value = "文件MD5", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    private String md5;

    @ApiModelProperty(value = "视频时长(秒)", required = true, example = "300")
    private Integer video_duration;

    @ApiModelProperty(value = "视频封面图URL", example = "http://example.com/cover.jpg")
    private String imgurl;

    @ApiModelProperty(value = "文件大小", required = true, example = "104857600")
    private Integer fileSize;
}
