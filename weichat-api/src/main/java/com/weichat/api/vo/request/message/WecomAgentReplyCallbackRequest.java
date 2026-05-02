package com.weichat.api.vo.request.message;

import com.weichat.api.vo.callback.ReplyMediaItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Wecom agent async reply callback request")
public class WecomAgentReplyCallbackRequest {

    @ApiModelProperty(value = "Whether the agent turn completed successfully", required = true, example = "true")
    private Boolean ok;

    @ApiModelProperty(value = "Wecom account uuid. Optional when sender-side user id can resolve the account", example = "uuid-xxxx-xxxx")
    private String uuid;

    @ApiModelProperty(value = "Reply sender user id. Usually the first-round receiver / wecom account user id", example = "123456789")
    private Long replySender;

    @ApiModelProperty(value = "Reply receiver user id. Usually the first-round sender / customer user id", example = "987654321")
    private Long replyReceiver;

    @ApiModelProperty(value = "Reply-side account user id used to resolve uuid when uuid is omitted", example = "123456789")
    private Long replyAccountUserId;

    @ApiModelProperty(value = "Legacy alias of reply sender user id for backward compatibility", example = "123456789")
    private Long sender;

    @ApiModelProperty(value = "Legacy alias of reply receiver user id for backward compatibility", example = "987654321")
    private Long receiver;

    @ApiModelProperty(value = "Legacy sender-side account user id used to resolve uuid when uuid is omitted", example = "123456789")
    private Long accountUserId;

    @ApiModelProperty(value = "Legacy sender-side user id alias used to resolve uuid", example = "123456789")
    private Long receiverUserId;

    @ApiModelProperty(value = "Customer service id", example = "456789123")
    private Long kfId;

    @ApiModelProperty(value = "Whether the target is a room", example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "Room id when isRoom=true", example = "1234567890123456789")
    private String roomId;

    @ApiModelProperty(value = "Text reply content", example = "hello")
    private String reply;

    @ApiModelProperty(value = "Image reply items")
    private List<ReplyMediaItem> images;

    @ApiModelProperty(value = "Voice reply items")
    private List<ReplyMediaItem> voices;
}
