package com.weichat.api.vo.callback;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplyMediaItemCompatibilityTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeVoiceTimeWithCamelCaseByJackson() throws Exception {
        ReplyMediaItem item = objectMapper.readValue("{\"voiceTime\":4}", ReplyMediaItem.class);
        assertEquals(Integer.valueOf(4), item.getVoiceTime());
    }

    @Test
    void shouldDeserializeVoiceTimeWithSnakeCaseByJackson() throws Exception {
        ReplyMediaItem item = objectMapper.readValue("{\"voice_time\":4}", ReplyMediaItem.class);
        assertEquals(Integer.valueOf(4), item.getVoiceTime());
    }

    @Test
    void shouldDeserializeVoiceTimeWithCamelCaseByFastjson() {
        ReplyMediaItem item = JSON.parseObject("{\"voiceTime\":4}", ReplyMediaItem.class);
        assertEquals(Integer.valueOf(4), item.getVoiceTime());
    }

    @Test
    void shouldDeserializeVoiceTimeWithSnakeCaseByFastjson() {
        ReplyMediaItem item = JSON.parseObject("{\"voice_time\":4}", ReplyMediaItem.class);
        assertEquals(Integer.valueOf(4), item.getVoiceTime());
    }
}
