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
 * 编辑标签请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "编辑标签请求参数")
public class EditLabelRequest extends BaseRequest {

    @ApiModelProperty(value = "标签ID", required = true, example = "label_id_xxx")
    private String labelId;

    @ApiModelProperty(value = "新标签名称", required = true, example = "普通客户")
    private String labelName;
}
