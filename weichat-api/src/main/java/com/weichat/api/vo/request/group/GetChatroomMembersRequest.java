package com.weichat.api.vo.request.group;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 获取客户群列表请求参数。
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "获取客户群列表请求参数")
public class GetChatroomMembersRequest extends BaseRequest {

    @ApiModelProperty(value = "每次查询数量", required = true, example = "5")
    private Integer limit;

    @ApiModelProperty(value = "分页起始下标，首次默认传 0，后续传上次返回的 next_start", required = true, example = "0")
    private Integer star_index;
}
