package com.weichat.api.vo.request.moment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发布朋友圈请求V2（支持多种内容类型组合）
 *
 * @author weichat
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "发布朋友圈请求参数V2")
public class PostPyqV2Request {

    @ApiModelProperty(value = "设备UUID", required = true, example = "2b0863724106a1160212bd1ccf025295")
    private String uuid;

    @ApiModelProperty(value = "朋友圈文字内容", example = "测试朋友圈发送")
    private String content;

    @ApiModelProperty(value = "文件内容列表（图片、视频、封面图）")
    private List<FileContent> fileContent;

    @ApiModelProperty(value = "可见用户ID列表", example = "[7881301488924502]")
    private List<Long> vids;

    @ApiModelProperty(value = "链接信息（发送链接朋友圈时使用）")
    private LinkInfo linkInfo;

    /**
     * 文件内容（用户输入）
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "文件内容")
    public static class FileContent {

        @ApiModelProperty(value = "文件类型：1-图片，2-视频，3-视频封面图", required = true, example = "1")
        private Integer type;

        @ApiModelProperty(value = "文件URL地址（支持http/https）", required = true, example = "https://example.com/image.jpg")
        private String url;

        // 以下字段由后端上传CDN后自动填充，用户无需传入
        @ApiModelProperty(hidden = true)
        private String fileid;

        @ApiModelProperty(hidden = true)
        private String md5;

        @ApiModelProperty(hidden = true)
        private Integer size;

        @ApiModelProperty(hidden = true)
        private Integer width;

        @ApiModelProperty(hidden = true)
        private Integer height;

        @ApiModelProperty(hidden = true)
        private Integer videoLen;
    }

    /**
     * 链接信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "链接信息")
    public static class LinkInfo {

        @ApiModelProperty(value = "链接标题", required = true)
        private String title;

        @ApiModelProperty(value = "链接URL", required = true)
        private String contentUrl;
    }
}
