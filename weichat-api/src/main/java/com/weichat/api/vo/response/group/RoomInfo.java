package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 群信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "群信息")
public class RoomInfo {

    @ApiModelProperty(value = "群ID", example = "123456789")
    private Long roomId;

    @ApiModelProperty(value = "群名称", example = "测试群")
    private String name;

    @ApiModelProperty(value = "群头像URL", example = "https://example.com/room.jpg")
    private String headImg;

    @ApiModelProperty(value = "群公告", example = "群公告内容")
    private String notice;

    @ApiModelProperty(value = "群主ID", example = "987654321")
    private Long ownerVid;

    @ApiModelProperty(value = "群主昵称", example = "群主")
    private String ownerNickname;

    @ApiModelProperty(value = "群成员数量", example = "50")
    private Integer memberCount;

    @ApiModelProperty(value = "管理员ID列表")
    private List<Long> adminVids;

    @ApiModelProperty(value = "创建时间(时间戳)", example = "1640000000")
    private Long createTime;

    @ApiModelProperty(value = "是否全员禁言", example = "false")
    private Boolean isMute;

    @ApiModelProperty(value = "是否禁止修改群名", example = "false")
    private Boolean disableRename;

    @ApiModelProperty(value = "邀请确认模式", example = "false")
    private Boolean inviteConfirm;
}
