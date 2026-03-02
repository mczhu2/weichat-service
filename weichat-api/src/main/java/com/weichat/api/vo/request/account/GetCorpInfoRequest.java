package com.weichat.api.vo.request.account;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 获取企业信息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "获取企业信息请求参数")
public class GetCorpInfoRequest extends BaseRequest {

    @ApiModelProperty(value = "企业ID", required = true, example = "123456789")
    private Long corpid;
}
