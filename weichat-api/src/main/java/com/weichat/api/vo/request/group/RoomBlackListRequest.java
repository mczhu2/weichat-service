package com.weichat.api.vo.request.group;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 群黑名单操作请求参数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "群黑名单操作请求参数")
public class RoomBlackListRequest extends BaseRequest {

    @ApiModelProperty(value = "群 ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "用户 ID 列表", required = true, example = "[111,222,333]")
    private List<Long> vids;

    @ApiModelProperty(value = "操作类型(add/remove)", required = true, example = "add")
    private String action;
}
