package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 语音转文字请求
 *
 * @author weichat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "语音转文字请求")
public class SpeechToTextRequest extends BaseRequest {

    @ApiModelProperty(value = "消息ID", required = true, example = "12345678901234567890")
    private String msgId;

    @ApiModelProperty(value = "语音文件路径", example = "/path/to/voice.amr")
    private String voicePath;

    @ApiModelProperty(value = "语音URL", example = "https://example.com/voice.amr")
    private String voiceUrl;
}
