package com.weichat.api.vo.request.chattag;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 会话标签操作请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "会话标签操作请求参数")
public class SessionInTagRequest extends BaseRequest {

    @ApiModelProperty(value = "标签ID", required = true, example = "tag_id_xxx")
    private String tagId;

    @ApiModelProperty(value = "会话ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "操作类型(add/remove)", required = true, example = "add")
    private String action;
}
