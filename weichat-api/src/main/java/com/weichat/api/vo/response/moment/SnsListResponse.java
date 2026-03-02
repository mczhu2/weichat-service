package com.weichat.api.vo.response.moment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 朋友圈列表响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "朋友圈列表响应数据")
public class SnsListResponse {

    @ApiModelProperty(value = "是否还有更多", example = "true")
    private Boolean hasMore;

    @ApiModelProperty(value = "下一页maxId", example = "max_id_xxx")
    private String nextMaxId;

    @ApiModelProperty(value = "朋友圈列表")
    private List<SnsInfo> list;
}
