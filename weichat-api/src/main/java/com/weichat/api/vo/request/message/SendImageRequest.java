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
@ApiModel(description = "Send image message request")
public class SendImageRequest extends BaseRequest {

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

    @ApiModelProperty(value = "File size", required = true, example = "102400")
    private Integer fileSize;

    @ApiModelProperty(value = "Image width", example = "800")
    private Integer width;

    @ApiModelProperty(value = "Image height", example = "600")
    private Integer height;

    @ApiModelProperty(value = "Thumbnail height", example = "512")
    private Integer thumb_image_height;

    @ApiModelProperty(value = "Thumbnail width", example = "384")
    private Integer thumb_image_width;

    @ApiModelProperty(value = "Thumbnail file size", example = "15195")
    private Integer thumb_file_size;

    @ApiModelProperty(value = "Thumbnail file md5", example = "4331947dc5f67c0d8a2893653a3f0cee")
    private String thumb_file_md5;

    @ApiModelProperty(value = "HD flag", example = "1")
    private Integer is_hd;
}
