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
 * 设置我的群昵称请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "设置我的群昵称请求参数")
public class SetMyRoomNameRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "我的群昵称", required = true, example = "我的群昵称")
    private String nickname;
}
