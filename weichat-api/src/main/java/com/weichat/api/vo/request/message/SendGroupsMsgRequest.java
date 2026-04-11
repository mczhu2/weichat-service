package com.weichat.api.vo.request.message;

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
 * 多目标消息发送请求参数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "多目标消息发送请求参数")
public class SendGroupsMsgRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群 ID 列表", required = true, example = "[111,222,333]")
    private List<Long> vids;

    @ApiModelProperty(value = "是否发送给群", required = true, example = "true")
    private Boolean isroom;

    @ApiModelProperty(value = "消息列表[{type:0,content:'文本'},{type:14,cdnkey:'',aeskey:''}]", required = true, example = "[{\"type\":0,\"content\":\"文本\"}]")
    private String msg_list;
}
