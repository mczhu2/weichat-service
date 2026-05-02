package com.weichat.api.vo.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Normalized reply media payload.
 */
@Data
@ApiModel(description = "Reply media item")
public class ReplyMediaItem {

    /**
     * Remote media address.
     */
    @ApiModelProperty(value = "Remote media url", example = "https://example.com/image.png")
    private String url;

    /**
     * Base64 encoded media content.
     */
    @ApiModelProperty(value = "Base64 encoded media payload")
    private String base64;

    /**
     * Upload filename.
     */
    @ApiModelProperty(value = "Upload filename", example = "reply.png")
    private String filename;

    /**
     * Upload MIME type.
     */
    @ApiModelProperty(value = "Upload MIME type", example = "image/png")
    private String contentType;

    /**
     * HD flag used by image forwarding.
     */
    @ApiModelProperty(value = "HD flag for image forwarding", example = "1")
    private Integer isHd;

    /**
     * Voice duration in seconds.
     */
    @ApiModelProperty(value = "Voice duration in seconds", example = "10")
    private Integer voiceTime;

    /**
     * Fill a default filename when the callback omits it.
     */
    public void ensureFilename(String defaultFilename) {
        if (!StringUtils.hasText(filename)) {
            this.filename = defaultFilename;
        }
    }
}
