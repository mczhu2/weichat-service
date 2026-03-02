package com.weichat.api.vo.response.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会话信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "会话信息")
public class SessionInfo {

    @ApiModelProperty(value = "会话ID(用户ID或群ID)", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "会话名称", example = "张三")
    private String name;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String headImg;

    @ApiModelProperty(value = "是否群聊(true:群聊, false:单聊)", example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "最后消息内容", example = "你好")
    private String lastMsg;

    @ApiModelProperty(value = "最后消息时间(时间戳)", example = "1640000000")
    private Long lastMsgTime;

    @ApiModelProperty(value = "未读消息数", example = "5")
    private Integer unreadCount;

    @ApiModelProperty(value = "是否置顶", example = "false")
    private Boolean isTop;

    @ApiModelProperty(value = "是否免打扰", example = "false")
    private Boolean isShield;

    @ApiModelProperty(value = "是否折叠", example = "false")
    private Boolean isFold;
}
