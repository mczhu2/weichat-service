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
 * 会话标签ID请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "会话标签ID请求参数")
public class TagIdRequest extends BaseRequest {

    @ApiModelProperty(value = "标签ID", required = true, example = "tag_id_xxx")
    private String tagId;
}
