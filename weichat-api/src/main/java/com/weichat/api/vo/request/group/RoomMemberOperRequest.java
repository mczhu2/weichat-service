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
 * 群成员操作请求参数，适用于邀请、移除和管理员变更。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "群成员操作请求参数")
public class RoomMemberOperRequest extends BaseRequest {

    @ApiModelProperty(value = "群 ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "成员 ID 列表", required = true, example = "[111,222,333]")
    private List<Long> vids;
}
