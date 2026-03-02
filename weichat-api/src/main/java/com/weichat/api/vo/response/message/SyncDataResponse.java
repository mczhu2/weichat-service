package com.weichat.api.vo.response.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 同步数据响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "同步数据响应")
public class SyncDataResponse {

    @ApiModelProperty(value = "是否还有更多数据", example = "true")
    private Boolean hasMore;

    @ApiModelProperty(value = "下次查询的序号", example = "100")
    private Integer nextSeq;

    @ApiModelProperty(value = "消息列表")
    private List<MessageInfo> messages;
}
