package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送表情消息请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送表情消息请求")
public class SendEmotionRequest extends BaseRequest {

    @ApiModelProperty(value = "接收者ID", required = true, example = "wxid_xxx")
    private String toId;

    @ApiModelProperty(value = "表情MD5", required = true, example = "abc123def456")
    private String md5;

    @ApiModelProperty(value = "表情大小", example = "1024")
    private Integer len;

    @ApiModelProperty(value = "表情类型(1:gif, 2:emoji)", example = "1")
    private Integer type;
}
