package com.weichat.api.vo.request.tag;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 标签批量加人请求参数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "标签批量加人请求参数")
public class LabelAddUserRequest extends BaseRequest {

    @ApiModelProperty(value = "标签 ID", required = true, example = "label_id_xxx")
    private String labelId;

    @ApiModelProperty(value = "用户 ID 列表", required = true, example = "[111,222,333]")
    private List<Long> vids;
}
