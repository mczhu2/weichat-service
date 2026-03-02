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
 * 发送文本+表情消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送文本+表情消息请求参数")
public class SendTextAndExpRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群ID", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "是否群消息", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "消息内容数组[{msgtype:0,msg:'文本'},{msgtype:3,msg:'[微笑]'}]", required = true, example = "[{\"msgtype\":0,\"msg\":\"文本\"},{\"msgtype\":3,\"msg\":\"[微笑]\"}]")
    private String content;
}
