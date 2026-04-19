package com.weichat.api.vo.request.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 群发计划创建请求。
 * 计划只负责描述周期和窗口，真正执行时会生成 mass_task + mass_task_detail。
 */
@Data
@ApiModel(description = "Mass task plan create request")
public class MassTaskPlanCreateRequest {

    @ApiModelProperty(value = "Plan name", example = "weekday campaign")
    private String planName;

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

    @ApiModelProperty(value = "Remark", example = "weekday batch send")
    private String remark;

    @ApiModelProperty(value = "Template id", example = "1001")
    private Long templateId;

    @ApiModelProperty(value = "Message payload", required = true)
    private MassTaskPayload payload;

    @ApiModelProperty(value = "Schedule type: 1=once, 2=daily, 3=weekly, 4=monthly", required = true, example = "3")
    private Integer scheduleType;

    @ApiModelProperty(value = "Weekly weekdays, 1=Monday ... 7=Sunday", example = "[1,2,3,4]")
    private List<Integer> weekdays;

    @ApiModelProperty(value = "Monthly days, supports -1 for last day", example = "[1,15,-1]")
    private List<Integer> monthDays;

    @ApiModelProperty(value = "Effective start time", required = true, example = "2026-04-20T09:00:00")
    private LocalDateTime effectiveStartAt;

    @ApiModelProperty(value = "Effective end time", example = "2026-12-31T23:59:59")
    private LocalDateTime effectiveEndAt;

    @ApiModelProperty(value = "Daily window start time", required = true, example = "09:00:00")
    private String windowStartTime;

    @ApiModelProperty(value = "Daily window end time", required = true, example = "18:00:00")
    private String windowEndTime;

    @ApiModelProperty(value = "Max receiver count per minute", example = "10")
    private Integer ratePerMinute;

    @ApiModelProperty(value = "Timezone", example = "Asia/Shanghai")
    private String timezone;
}
