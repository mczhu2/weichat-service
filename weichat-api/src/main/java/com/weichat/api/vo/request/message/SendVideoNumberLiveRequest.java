package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送视频号直播消息请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送视频号直播消息请求")
public class SendVideoNumberLiveRequest extends BaseRequest {

    @ApiModelProperty(value = "接收者ID", required = true, example = "wxid_xxx")
    private String toId;

    @ApiModelProperty(value = "视频号ID", required = true, example = "finder_xxx")
    private String finderId;

    @ApiModelProperty(value = "直播间ID", required = true, example = "live_xxx")
    private String liveId;

    @ApiModelProperty(value = "直播间标题", example = "精彩直播")
    private String title;

    @ApiModelProperty(value = "主播名称", example = "主播小王")
    private String anchorName;

    @ApiModelProperty(value = "直播封面", example = "https://example.com/cover.jpg")
    private String coverUrl;
}
