package com.weichat.api.vo.response.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 内部联系人列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "内部联系人列表响应数据")
public class InnerContactListResponse {

    @ApiModelProperty(value = "联系人总数", example = "100")
    private Integer total;

    @ApiModelProperty(value = "下一次查询下标，首次传空字符串，后续传上次返回的seq", example = "6758970362530103297_6564583924104691719_3_6564552596235747331_0_7615047555990159361_0")
    private String seq;

    @ApiModelProperty(value = "联系人列表")
    private List<ContactInfo> list;
}
