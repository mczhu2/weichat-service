package com.weichat.api.vo.request.message;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 发送小程序消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送小程序消息请求参数")
public class SendAppRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群ID", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "是否群消息", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "小程序标题", required = true, example = "小程序标题")
    private String title;

    @ApiModelProperty(value = "小程序描述", required = true, example = "小程序描述信息")
    private String desc;

    @ApiModelProperty(value = "小程序名称", required = true, example = "我的小程序")
    private String appName;

    @ApiModelProperty(value = "小程序AppID", required = true, example = "wx1234567890")
    private String appid;

    @ApiModelProperty(value = "小程序原始ID", required = true, example = "gh_1234567890")
    private String username;

    @ApiModelProperty(value = "小程序跳转页面", required = true, example = "pages/index/index")
    private String pagepath;

    @ApiModelProperty(value = "小程序头像URL", example = "https://example.com/icon.png")
    private String weappIconUrl;
}
