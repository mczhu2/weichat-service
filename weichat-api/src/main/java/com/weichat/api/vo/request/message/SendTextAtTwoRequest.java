package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 发送@文本消息(高级)请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送@文本消息(高级)请求")
public class SendTextAtTwoRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID", required = true, example = "room_xxx")
    private String roomId;

    @ApiModelProperty(value = "消息内容", required = true, example = "大家好")
    private String content;

    @ApiModelProperty(value = "@用户ID列表", example = "[\"wxid_xxx\", \"wxid_yyy\"]")
    private List<String> atUserIds;

    @ApiModelProperty(value = "@用户VID列表", example = "[123456, 789012]")
    private List<Long> atVids;

    @ApiModelProperty(value = "是否@所有人", example = "false")
    private Boolean atAll;
}
