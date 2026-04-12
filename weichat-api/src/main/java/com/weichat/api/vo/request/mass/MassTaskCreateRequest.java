package com.weichat.api.vo.request.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "Rich mass task create request")
public class MassTaskCreateRequest {

    @ApiModelProperty(value = "Task name", example = "spring campaign")
    private String taskName;

    @ApiModelProperty(value = "Task type: 1=user, 2=group", required = true, example = "1")
    private Integer taskType;

    @ApiModelProperty(value = "Receiver type: 1=user, 2=group", required = true, example = "1")
    private Integer receiverType;

    @ApiModelProperty(value = "Receiver ids", required = true, example = "[510,512]")
    private List<Long> receiverIds;

    @ApiModelProperty(value = "Message type: 0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app", required = true, example = "0")
    private Integer msgType;

    @ApiModelProperty(value = "Creator or device uuid", example = "1753cdff-0501-42fe-bb5a-2a4b9629f7fb")
    private String creator;

    @ApiModelProperty(value = "Planned send time", example = "2026-04-13T10:00:00")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "Remark", example = "draft")
    private String remark;

    @ApiModelProperty(value = "Template id", example = "1001")
    private Long templateId;

    @ApiModelProperty(value = "Message payload", required = true)
    private MassTaskPayload payload;
}
