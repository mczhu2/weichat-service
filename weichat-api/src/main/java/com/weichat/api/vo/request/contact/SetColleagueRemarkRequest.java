package com.weichat.api.vo.request.contact;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 设置同事备注请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "设置同事备注请求参数")
public class SetColleagueRemarkRequest extends BaseRequest {

    @ApiModelProperty(value = "同事ID", required = true, example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "备注名", required = true, example = "李四")
    private String remark;
}
