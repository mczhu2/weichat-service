package com.weichat.api.vo.request.tag;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户添加标签请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户添加标签请求参数")
public class UserAddLabelsRequest extends BaseRequest {

    @ApiModelProperty(value = "用户ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "标签ID列表", required = true, example = "[\"label1\",\"label2\"]")
    private String labelIds;
}
