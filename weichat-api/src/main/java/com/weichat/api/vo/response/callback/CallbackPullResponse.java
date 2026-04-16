package com.weichat.api.vo.response.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 回拉消息记录响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "回拉消息记录响应数据")
public class CallbackPullResponse {

    @ApiModelProperty(value = "回调任务JSON内容列表")
    private List<String> jsonContentList;

    @ApiModelProperty(value = "是否还有更多数据", example = "true")
    private Boolean hasMore;

    @ApiModelProperty(value = "当前拉取的最大消息ID")
    private Long currentMaxId;
}
