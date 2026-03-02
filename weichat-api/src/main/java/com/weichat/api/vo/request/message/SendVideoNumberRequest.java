package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送视频号消息请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送视频号消息请求")
public class SendVideoNumberRequest extends BaseRequest {

    @ApiModelProperty(value = "接收者ID", required = true, example = "wxid_xxx")
    private String toId;

    @ApiModelProperty(value = "视频号ID", required = true, example = "finder_xxx")
    private String finderId;

    @ApiModelProperty(value = "视频号名称", example = "视频号主页")
    private String finderName;

    @ApiModelProperty(value = "视频号头像", example = "https://example.com/avatar.jpg")
    private String finderAvatar;

    @ApiModelProperty(value = "视频标题", example = "精彩视频")
    private String title;

    @ApiModelProperty(value = "视频封面", example = "https://example.com/cover.jpg")
    private String coverUrl;

    @ApiModelProperty(value = "视频链接", example = "https://example.com/video.mp4")
    private String videoUrl;
}
