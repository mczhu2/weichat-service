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
 * 发送位置消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送位置消息请求参数")
public class SendLocationRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群ID", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "是否群消息", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "经度", required = true, example = "116.404")
    private Double longitude;

    @ApiModelProperty(value = "纬度", required = true, example = "39.915")
    private Double latitude;

    @ApiModelProperty(value = "地址名称", required = true, example = "天安门广场")
    private String address;

    @ApiModelProperty(value = "详细地址", required = true, example = "北京市东城区天安门广场")
    private String detailed_address;
}
