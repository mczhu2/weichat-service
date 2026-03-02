package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送名片消息请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送名片消息请求")
public class SendBusinessCardRequest extends BaseRequest {

    @ApiModelProperty(value = "接收者ID", required = true, example = "wxid_xxx")
    private String toId;

    @ApiModelProperty(value = "名片用户ID", required = true, example = "card_user_xxx")
    private String cardUserId;

    @ApiModelProperty(value = "名片用户VID", example = "123456789")
    private Long cardVid;

    @ApiModelProperty(value = "名片用户昵称", example = "张三")
    private String cardNickname;

    @ApiModelProperty(value = "名片类型(1:个人, 2:公众号)", example = "1")
    private Integer cardType;
}
