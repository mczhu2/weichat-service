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
 * 标签添加用户请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "标签添加用户请求参数")
public class LabelAddUserRequest extends BaseRequest {

    @ApiModelProperty(value = "标签ID", required = true, example = "label_id_xxx")
    private String labelId;

    @ApiModelProperty(value = "用户ID列表", required = true, example = "[111,222,333]")
    private String vids;
}
