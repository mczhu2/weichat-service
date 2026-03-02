package com.weichat.api.vo.response.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "消息信息")
public class MessageInfo {

    @ApiModelProperty(value = "消息ID", example = "12345")
    private Long msgId;

    @ApiModelProperty(value = "服务端消息ID", example = "server_msg_123")
    private String serverMsgId;

    @ApiModelProperty(value = "发送者ID", example = "123456789")
    private Long fromVid;

    @ApiModelProperty(value = "接收者ID", example = "987654321")
    private Long toVid;

    @ApiModelProperty(value = "群ID(群消息时有值)", example = "111222333")
    private Long roomId;

    @ApiModelProperty(value = "消息类型(0:文本, 1:图片, 2:语音, 3:视频, 4:文件, 5:链接, 6:小程序, 7:名片, 8:位置)", example = "0")
    private Integer msgType;

    @ApiModelProperty(value = "消息内容", example = "你好")
    private String content;

    @ApiModelProperty(value = "CDN Key(媒体消息)", example = "cdn_key_xxx")
    private String cdnKey;

    @ApiModelProperty(value = "AES Key(媒体消息)", example = "aes_key_xxx")
    private String aesKey;

    @ApiModelProperty(value = "文件大小(字节)", example = "102400")
    private Integer fileSize;

    @ApiModelProperty(value = "发送时间(时间戳)", example = "1640000000")
    private Long createTime;

    @ApiModelProperty(value = "是否已读", example = "true")
    private Boolean isRead;

    @ApiModelProperty(value = "是否撤回", example = "false")
    private Boolean isRevoke;
}
