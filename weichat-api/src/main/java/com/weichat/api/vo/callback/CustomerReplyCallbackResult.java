package com.weichat.api.vo.callback;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Normalized callback result used by the auto reply flow.
 */
@Data
public class CustomerReplyCallbackResult {

    /**
     * Downstream callback success flag.
     */
    private Boolean ok;

    /**
     * Plain text reply content.
     */
    private String reply;

    /**
     * Normalized image reply items.
     */
    private List<ReplyMediaItem> images = Collections.emptyList();

    /**
     * Normalized voice reply items.
     */
    private List<ReplyMediaItem> voices = Collections.emptyList();

    /**
     * Unified success check for service layer.
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(ok);
    }
}
