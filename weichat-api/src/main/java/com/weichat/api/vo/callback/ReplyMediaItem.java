package com.weichat.api.vo.callback;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Normalized reply media payload.
 */
@Data
public class ReplyMediaItem {

    /**
     * Remote media address.
     */
    private String url;

    /**
     * Base64 encoded media content.
     */
    private String base64;

    /**
     * Upload filename.
     */
    private String filename;

    /**
     * Upload MIME type.
     */
    private String contentType;

    /**
     * HD flag used by image forwarding.
     */
    private Integer isHd;

    /**
     * Voice duration in seconds.
     */
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
