package com.weichat.api.support.mass;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mass message type definitions used by task creation and UI schema.
 */
public final class MassMessageType {

    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    public static final int FILE = 2;
    public static final int VOICE = 3;
    public static final int VIDEO = 4;
    public static final int LINK = 5;
    public static final int APP = 6;

    private static final Map<Integer, String> LABELS;

    static {
        Map<Integer, String> labels = new LinkedHashMap<>();
        labels.put(TEXT, "文本");
        labels.put(IMAGE, "图片");
        labels.put(FILE, "文件");
        labels.put(VOICE, "音频");
        labels.put(VIDEO, "视频");
        labels.put(LINK, "链接卡片");
        labels.put(APP, "小程序消息");
        LABELS = Collections.unmodifiableMap(labels);
    }

    private MassMessageType() {
    }

    public static boolean contains(Integer msgType) {
        return msgType != null && LABELS.containsKey(msgType);
    }

    public static String labelOf(Integer msgType) {
        return LABELS.getOrDefault(msgType, "未知类型");
    }
}
