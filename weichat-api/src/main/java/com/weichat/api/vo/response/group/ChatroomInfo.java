package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户群信息。
 *
 * @author weichat
 */
@Data
@ApiModel(description = "客户群信息")
public class ChatroomInfo {

    @ApiModelProperty(value = "客户群ID", example = "10696052955013024")
    private Long room_id;

    @ApiModelProperty(value = "创建人ID", example = "0")
    private Long create_user_id;

    @ApiModelProperty(value = "群信息票据", example = "9FE241BC08F50F9A2FCBA692759B6FD8A5EA88B107787838")
    private String infoticket;

    @ApiModelProperty(value = "更新时间", example = "1650135314")
    private Long update_time;

    @ApiModelProperty(value = "群成员数量", example = "4")
    private Integer total;

    @ApiModelProperty(value = "创建时间", example = "1648889598")
    private Long create_time;

    @ApiModelProperty(value = "群名称", example = "客户沟通群")
    private String nickname;

    @ApiModelProperty(value = "群头像URL", example = "https://wework.qpic.cn/wwpic/699045_06DbkaBSSVeLfTg_1652088785/0")
    private String roomurl;
}
