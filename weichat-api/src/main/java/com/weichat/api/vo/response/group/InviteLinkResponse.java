package com.weichat.api.vo.response.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请链接响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "邀请链接响应数据")
public class InviteLinkResponse {

    @ApiModelProperty(value = "邀请链接", example = "https://work.weixin.qq.com/wework_admin/gencode/xxx")
    private String inviteLink;

    @ApiModelProperty(value = "二维码图片URL", example = "https://example.com/qrcode.png")
    private String qrCodeUrl;

    @ApiModelProperty(value = "过期时间(时间戳)", example = "1640086400")
    private Long expireTime;
}
