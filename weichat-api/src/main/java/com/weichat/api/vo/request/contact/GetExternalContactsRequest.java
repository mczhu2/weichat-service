package com.weichat.api.vo.request.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 获取外部联系人列表请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ApiModel(description = "获取外部联系人列表请求参数")
public class GetExternalContactsRequest {

    @ApiModelProperty(value = "实例唯一标识", required = true, example = "3240fde0-45e2-48c0-90e8-cb098d0ebe43")
    private String uuid;

    @ApiModelProperty(value = "每页返回的数量", required = true, example = "100")
    private Integer limit;

    @ApiModelProperty(value = "查询下标，首次传0，后续传上次返回的seq", required = true, example = "0")
    private Long seq;
}
