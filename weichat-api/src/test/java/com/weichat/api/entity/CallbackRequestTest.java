package com.weichat.api.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallbackRequestTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldStringifyObjectJsonDuringJacksonDeserialization() throws Exception {
        String payload = "{\"uuid\":\"instance-001\",\"type\":\"message\",\"json\":{\"reply\":\"hello\",\"count\":2,\"ok\":true}}";

        CallbackRequest request = objectMapper.readValue(payload, CallbackRequest.class);
        JsonNode callbackJson = objectMapper.readTree(request.getJson());

        assertEquals("instance-001", request.getUuid());
        assertEquals("message", request.getType());
        assertEquals("hello", callbackJson.get("reply").asText());
        assertEquals(2, callbackJson.get("count").asInt());
        assertEquals(true, callbackJson.get("ok").asBoolean());
    }

    @Test
    void shouldKeepStringJsonUnchangedDuringJacksonDeserialization() throws Exception {
        String rawJson = "{\"reply\":\"hello\",\"count\":2,\"ok\":true}";
        String payload = "{\"uuid\":\"instance-002\",\"type\":\"event\",\"json\":"
                + objectMapper.writeValueAsString(rawJson) + "}";

        CallbackRequest request = objectMapper.readValue(payload, CallbackRequest.class);

        assertEquals("instance-002", request.getUuid());
        assertEquals("event", request.getType());
        assertEquals(rawJson, request.getJson());
    }
}
