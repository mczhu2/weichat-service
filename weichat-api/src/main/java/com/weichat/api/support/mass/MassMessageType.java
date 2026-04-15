package com.weichat.api.support.mass;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MassMessageType {

    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    public static final int FILE = 2;
    public static final int VOICE = 3;
    public static final int VIDEO = 4;
    public static final int LINK = 5;
    public static final int APP = 6;
    public static final int COMPOSITE = 7;

    private static final Map<Integer, String> LABELS;

    static {
        Map<Integer, String> labels = new LinkedHashMap<>();
        labels.put(TEXT, "text");
        labels.put(IMAGE, "image");
        labels.put(FILE, "file");
        labels.put(VOICE, "voice");
        labels.put(VIDEO, "video");
        labels.put(LINK, "link");
        labels.put(APP, "app");
        labels.put(COMPOSITE, "composite");
        LABELS = Collections.unmodifiableMap(labels);
    }

    private MassMessageType() {
    }

    public static boolean contains(Integer msgType) {
        return msgType != null && LABELS.containsKey(msgType);
    }

    public static String labelOf(Integer msgType) {
        return LABELS.getOrDefault(msgType, "unknown");
    }
}
