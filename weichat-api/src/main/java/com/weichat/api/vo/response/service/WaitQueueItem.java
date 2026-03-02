package com.weichat.api.vo.response.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 等待队列项
 *
 * @author weichat
 */
@Data
@ApiModel(description = "等待队列项")
public class WaitQueueItem {

    @ApiModelProperty(value = "客户ID", example = "customer_id_xxx")
    private String customerId;

    @ApiModelProperty(value = "客户昵称", example = "客户小王")
    private String customerName;

    @ApiModelProperty(value = "客户头像", example = "https://example.com/avatar.jpg")
    private String customerAvatar;

    @ApiModelProperty(value = "等待时间(秒)", example = "120")
    private Integer waitTime;

    @ApiModelProperty(value = "进入队列时间(时间戳)", example = "1640000000")
    private Long enterTime;
}
