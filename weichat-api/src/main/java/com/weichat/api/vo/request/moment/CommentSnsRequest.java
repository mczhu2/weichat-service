package com.weichat.api.vo.request.moment;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 评论朋友圈请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "评论朋友圈请求参数")
public class CommentSnsRequest extends BaseRequest {

    @ApiModelProperty(value = "朋友圈ID", required = true, example = "sns_id_xxx")
    private String snsId;

    @ApiModelProperty(value = "评论内容", required = true, example = "很漂亮!")
    private String content;

    @ApiModelProperty(value = "回复的评论ID(回复评论时使用)", example = "comment_id_xxx")
    private String replyCommentId;
}
