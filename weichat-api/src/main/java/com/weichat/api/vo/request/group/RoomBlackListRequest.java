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
 * 添加/移除群黑名单请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "添加/移除群黑名单请求参数")
public class RoomBlackListRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "用户ID列表", required = true, example = "[111,222,333]")
    private String vids;

    @ApiModelProperty(value = "操作类型(add/remove)", required = true, example = "add")
    private String action;
}
