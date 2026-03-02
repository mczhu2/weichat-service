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
 * 点赞/取消点赞请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "点赞/取消点赞请求参数")
public class LikeOrUnlikeRequest extends BaseRequest {

    @ApiModelProperty(value = "朋友圈ID", required = true, example = "sns_id_xxx")
    private String snsId;

    @ApiModelProperty(value = "是否点赞(true:点赞, false:取消)", required = true, example = "true")
    private Boolean like;
}
