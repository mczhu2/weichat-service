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
 * 删除评论请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "删除评论请求参数")
public class DeleteCommentRequest extends BaseRequest {

    @ApiModelProperty(value = "朋友圈ID", required = true, example = "sns_id_xxx")
    private String snsId;

    @ApiModelProperty(value = "评论ID", required = true, example = "comment_id_xxx")
    private String commentId;
}
