package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "Send voice message request")
public class SendVoiceRequest extends BaseRequest {

    @ApiModelProperty(value = "Receiver user id or room id", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "Customer service id", example = "987654321")
    private Long kf_id;

    @ApiModelProperty(value = "Whether the target is a room", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "CDN key", required = true, example = "cdn_key_xxx")
    private String cdnkey;

    @ApiModelProperty(value = "AES key", required = true, example = "aes_key_xxx")
    private String aeskey;

    @ApiModelProperty(value = "File MD5", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    private String md5;

    @ApiModelProperty(value = "Voice duration in seconds", required = true, example = "10")
    private Integer voice_time;

    @ApiModelProperty(value = "File size", required = true, example = "102400")
    private Integer fileSize;
}
