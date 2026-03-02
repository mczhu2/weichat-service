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
 * 撤回消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "撤回消息请求参数")
public class RevokeMsgRequest extends BaseRequest {

    @ApiModelProperty(value = "要撤回的消息ID", required = true, example = "12345")
    private Integer msgid;

    @ApiModelProperty(value = "群ID(群消息填群ID，联系人消息填0)", required = true, example = "0")
    private Long roomid;
}
