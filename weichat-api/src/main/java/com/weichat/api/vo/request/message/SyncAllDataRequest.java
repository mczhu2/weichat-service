package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 同步所有数据请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "同步所有数据请求参数")
public class SyncAllDataRequest extends BaseRequest {

    @ApiModelProperty(value = "每次返回大小", required = true, example = "100")
    private Integer limit;

    @ApiModelProperty(value = "查询下标(建议传消息回调中的最后一次server_id)", required = true, example = "0")
    private Integer seq;
}
