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
 * 发布朋友圈请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发布朋友圈请求参数")
public class PostPyqRequest extends BaseRequest {

    @ApiModelProperty(value = "朋友圈文字内容", required = true, example = "今天天气真好")
    private String content;

    @ApiModelProperty(value = "图片列表(CDN信息)", example = "[{\"cdnkey\":\"xxx\",\"aeskey\":\"yyy\"}]")
    private String images;

    @ApiModelProperty(value = "可见用户ID列表", example = "[111,222,333]")
    private String visibleVids;
}
