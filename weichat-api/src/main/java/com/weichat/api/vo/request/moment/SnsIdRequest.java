package com.weichat.api.vo.request.moment;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 朋友圈ID请求(通用)
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "朋友圈ID请求参数")
public class SnsIdRequest extends BaseRequest {

    @ApiModelProperty(value = "朋友圈ID", required = true, example = "sns_id_xxx")
    private String snsId;
}
