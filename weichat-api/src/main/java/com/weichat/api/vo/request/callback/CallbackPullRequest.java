package com.weichat.api.vo.request.callback;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 回拉消息记录请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "回拉消息记录请求参数")
public class CallbackPullRequest extends BaseRequest {

    @ApiModelProperty(value = "数据范围类型: 0-从新开始拉取(全量), 1-拉取增量数据", required = true, example = "1")
    private Integer dataRangeType;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageNum;
}
