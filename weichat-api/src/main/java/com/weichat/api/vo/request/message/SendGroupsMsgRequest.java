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
 * 发送多群消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送多群消息请求参数")
public class SendGroupsMsgRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID或好友ID列表(不能混填)", required = true, example = "[111,222,333]")
    private String vids;

    @ApiModelProperty(value = "是否发送给群", required = true, example = "true")
    private Boolean isroom;

    @ApiModelProperty(value = "消息列表[{type:0,content:'文本'},{type:14,cdnkey:'',aeskey:''}]", required = true, example = "[{\"type\":0,\"content\":\"文本\"}]")
    private String msg_list;
}
