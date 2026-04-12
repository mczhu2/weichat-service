package com.weichat.api.vo.request.message;

import com.weichat.api.vo.callback.ReplyMediaItem;
import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "Send friend reply request")
public class SendFriendReplyRequest extends BaseRequest {

    @ApiModelProperty(value = "Sender user id", required = true, example = "123456789")
    private Long sender;

    @ApiModelProperty(value = "Receiver user id", required = true, example = "987654321")
    private Long receiver;

    @ApiModelProperty(value = "Customer service id", example = "456789123")
    private Long kfId;

    @ApiModelProperty(value = "Text reply content", example = "你好")
    private String reply;

    @ApiModelProperty(value = "Image reply items")
    private List<ReplyMediaItem> images;

    @ApiModelProperty(value = "Voice reply items")
    private List<ReplyMediaItem> voices;
}
