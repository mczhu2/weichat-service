package com.weichat.api.vo.callback;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 下游业务回调请求体里的消息内容模型。
 * content 只承载文本语义，媒体资源统一放到 medias 中。
 */
@Data
public class DownstreamCallbackPayload {

    /**
     * 文本内容，非文本消息允许为空。
     */
    private String content;

    /**
     * 结构化媒体列表，图片/视频/语音等资源统一放在这里。
     */
    private List<DownstreamMediaVo> medias = Collections.emptyList();

    /**
     * 判断当前消息是否具备可发送给下游的有效内容。
     */
    public boolean hasPayload() {
        return StringUtils.hasText(content) || !CollectionUtils.isEmpty(medias);
    }
}
