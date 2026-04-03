package com.weichat.api.vo.response.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 联系人列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "联系人列表响应数据")
public class ContactListResponse {

    @ApiModelProperty(value = "联系人总数", example = "100")
    private Integer total;

    @ApiModelProperty(value = "下一次查询下标，首次传0，后续传上次返回的seq", example = "8052931")
    private Long seq;

    @ApiModelProperty(value = "联系人列表")
    private List<ContactInfo> list;
}
