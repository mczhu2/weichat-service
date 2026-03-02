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
 * 发送链接消息请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "发送链接消息请求参数")
public class SendLinkRequest extends BaseRequest {

    @ApiModelProperty(value = "接收人或群ID", required = true, example = "123456789")
    private Long send_userid;

    @ApiModelProperty(value = "是否群消息", required = true, example = "false")
    private Boolean isRoom;

    @ApiModelProperty(value = "链接地址", required = true, example = "https://example.com")
    private String url;

    @ApiModelProperty(value = "链接标题", required = true, example = "文章标题")
    private String title;

    @ApiModelProperty(value = "链接描述", required = true, example = "这是一篇文章的描述")
    private String content;

    @ApiModelProperty(value = "链接封面图URL", example = "https://example.com/cover.jpg")
    private String imgurl;
}
