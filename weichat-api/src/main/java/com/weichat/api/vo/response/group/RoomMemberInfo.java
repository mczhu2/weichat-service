package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 群成员信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "群成员信息")
public class RoomMemberInfo {

    @ApiModelProperty(value = "成员ID", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "成员昵称", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "群内昵称", example = "小张")
    private String roomNickname;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String headImg;

    @ApiModelProperty(value = "角色(0:普通成员, 1:管理员, 2:群主)", example = "0")
    private Integer role;

    @ApiModelProperty(value = "入群时间(时间戳)", example = "1640000000")
    private Long joinTime;

    @ApiModelProperty(value = "邀请人ID", example = "987654321")
    private Long inviterVid;
}
