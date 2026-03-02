package com.weichat.api.vo.request.group;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 删除欢迎语请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "删除欢迎语请求")
public class DelWelcomeMsgRequest extends BaseRequest {

    @ApiModelProperty(value = "欢迎语ID", required = true, example = "welcome_msg_xxx")
    private String welcomeMsgId;
}
