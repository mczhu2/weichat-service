package com.weichat.api.vo.request.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 获取互联内部联系人请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ApiModel(description = "获取互联内部联系人请求参数")
public class GetHuLianInnerContactsRequest {

    @ApiModelProperty(value = "实例唯一标识", required = true, example = "3240fde0-45e2-48c0-90e8-cb098d0ebe43")
    private String uuid;

    @ApiModelProperty(value = "企业互联组ID", required = true, example = "8725724793033875")
    private Long circleId;

    @ApiModelProperty(value = "每次返回数量", required = true, example = "10")
    private Integer limit;

    @ApiModelProperty(value = "查询下标，首次传空字符串，后续传上次返回的seq", required = true, example = "")
    private String strSeq;
}
