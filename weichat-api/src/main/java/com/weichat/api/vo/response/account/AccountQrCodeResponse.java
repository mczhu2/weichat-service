package com.weichat.api.vo.response.account;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号二维码响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "账号二维码响应数据")
public class AccountQrCodeResponse {

    @ApiModelProperty(value = "二维码URL", example = "https://wework.qpic.cn/wwpic3az/852088_GGD-ecdyTj2nE5l_1775654952/0")
    @JSONField(name = "QrCodeUrl")
    private String qrCodeUrl;
}