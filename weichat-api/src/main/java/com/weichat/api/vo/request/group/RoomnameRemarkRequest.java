package com.weichat.api.vo.request.group;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 群名备注请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "群名备注请求参数")
public class RoomnameRemarkRequest extends BaseRequest {

    @ApiModelProperty(value = "群ID", required = true, example = "123456789")
    private Long roomid;

    @ApiModelProperty(value = "群名备注", required = true, example = "我的备注名")
    private String remark;
}
