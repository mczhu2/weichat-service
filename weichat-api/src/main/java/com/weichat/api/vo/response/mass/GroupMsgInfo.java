package com.weichat.api.vo.response.mass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 群发任务信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "群发任务信息")
public class GroupMsgInfo {

    @ApiModelProperty(value = "任务ID", example = "task_id_xxx")
    private String taskId;

    @ApiModelProperty(value = "消息ID", example = "msg_id_xxx")
    private String msgId;

    @ApiModelProperty(value = "消息类型", example = "0")
    private Integer msgType;

    @ApiModelProperty(value = "消息内容", example = "群发内容")
    private String content;

    @ApiModelProperty(value = "发送总数", example = "100")
    private Integer totalCount;

    @ApiModelProperty(value = "成功数", example = "95")
    private Integer successCount;

    @ApiModelProperty(value = "失败数", example = "5")
    private Integer failCount;

    @ApiModelProperty(value = "任务状态(0:待发送, 1:发送中, 2:已完成)", example = "2")
    private Integer status;

    @ApiModelProperty(value = "创建时间(时间戳)", example = "1640000000")
    private Long createTime;

    @ApiModelProperty(value = "完成时间(时间戳)", example = "1640003600")
    private Long finishTime;
}
