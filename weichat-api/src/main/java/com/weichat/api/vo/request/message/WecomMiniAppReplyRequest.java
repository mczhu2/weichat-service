package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "WeCom mini app reply request")
public class WecomMiniAppReplyRequest extends BaseRequest {

    @ApiModelProperty(value = "Reply sender user id, usually the WeCom account userId", example = "123456789")
    private Long replySender;

    @ApiModelProperty(value = "Reply receiver user id, usually the customer userId", example = "987654321")
    private Long replyReceiver;

    @ApiModelProperty(value = "Reply account user id, used to resolve uuid when uuid is empty", example = "123456789")
    private Long replyAccountUserId;

    @ApiModelProperty(value = "Alias for replySender", example = "123456789")
    private Long sender;

    @ApiModelProperty(value = "Alias for replyReceiver", example = "987654321")
    private Long receiver;

    @ApiModelProperty(value = "Alias for replyAccountUserId", example = "123456789")
    private Long accountUserId;

    @ApiModelProperty(value = "Account user id alias used to resolve uuid", example = "123456789")
    private Long receiverUserId;

    @ApiModelProperty(value = "Whether this is a room message", example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "Room id, required when isRoom=true", example = "1234567890123456789")
    private String roomId;

    @ApiModelProperty(value = "Optional customer service id", example = "123456789")
    private Long kfId;

    @ApiModelProperty(value = "Mini app title", required = true, example = "Job detail")
    private String title;

    @ApiModelProperty(value = "Mini app description, can be empty", example = "View job detail")
    private String desc;

    @ApiModelProperty(value = "Mini app name", required = true, example = "Recruit mini app")
    private String appName;

    @ApiModelProperty(value = "Mini app appid", required = true, example = "wx1234567890")
    private String appid;

    @ApiModelProperty(value = "Mini app username", required = true, example = "gh_1234567890@app")
    private String username;

    @ApiModelProperty(value = "Mini app page path", required = true, example = "pages/jobs/detail?id=1001")
    private String pagepath;

    @ApiModelProperty(value = "Mini app icon URL", example = "https://example.com/icon.png")
    private String weappIconUrl;

    @ApiModelProperty(value = "Mini app cover image URL, skipped when empty", example = "https://example.com/cover.png")
    private String coverUrl;
}
