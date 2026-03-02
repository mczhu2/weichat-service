package com.weichat.api.vo.response.moment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 朋友圈信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "朋友圈信息")
public class SnsInfo {

    @ApiModelProperty(value = "朋友圈ID", example = "sns_id_xxx")
    private String snsId;

    @ApiModelProperty(value = "发布者ID", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "发布者昵称", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "发布者头像", example = "https://example.com/avatar.jpg")
    private String headImg;

    @ApiModelProperty(value = "文字内容", example = "今天天气真好")
    private String content;

    @ApiModelProperty(value = "图片列表")
    private List<SnsMediaInfo> images;

    @ApiModelProperty(value = "视频信息")
    private SnsMediaInfo video;

    @ApiModelProperty(value = "链接信息")
    private SnsLinkInfo link;

    @ApiModelProperty(value = "点赞数", example = "10")
    private Integer likeCount;

    @ApiModelProperty(value = "评论数", example = "5")
    private Integer commentCount;

    @ApiModelProperty(value = "发布时间(时间戳)", example = "1640000000")
    private Long createTime;

    @ApiModelProperty(value = "是否已点赞", example = "false")
    private Boolean isLiked;
}
