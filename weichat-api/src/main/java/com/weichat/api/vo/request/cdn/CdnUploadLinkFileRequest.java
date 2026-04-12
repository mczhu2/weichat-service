package com.weichat.api.vo.request.cdn;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * CDN upload link file request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "CDN upload link file request")
public class CdnUploadLinkFileRequest extends BaseRequest {

    @JSONField(name = "url", alternateNames = {"fileUrl"})
    @JsonProperty("url")
    @JsonAlias("fileUrl")
    @ApiModelProperty(value = "File URL", required = true, example = "https://example.com/file.pdf")
    private String url;

    @JSONField(name = "filename", alternateNames = {"fileName"})
    @JsonProperty("filename")
    @JsonAlias("fileName")
    @ApiModelProperty(value = "File name", required = true, example = "document.pdf")
    private String filename;
}
