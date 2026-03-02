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
 * 获取朋友圈列表请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "获取朋友圈列表请求参数")
public class GetSnsListRequest extends BaseRequest {

    @ApiModelProperty(value = "用户ID(获取指定用户朋友圈)", example = "123456789")
    private Long vid;

    @ApiModelProperty(value = "分页参数(上次最后一条记录ID)", example = "max_id_xxx")
    private String maxId;
}
