package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.service.RemoteMediaDownloadService;
import com.weichat.api.service.RemoteMediaDownloadService.RemoteMediaResource;
import com.weichat.api.util.MessageTemplateUtil;
import com.weichat.api.vo.request.mass.MassTaskAppPayload;
import com.weichat.api.vo.request.mass.MassTaskLinkPayload;
import com.weichat.api.vo.request.mass.MassTaskPayload;
import com.weichat.api.vo.request.message.SendTextRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class MassTaskMessageSupport {

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private RemoteMediaDownloadService remoteMediaDownloadService;

    public JSONObject postMessage(String endpoint, Object request) {
        return wxWorkApiClient.post(endpoint, JSON.parseObject(JSON.toJSONString(request)));
    }

    public String resolveContent(MassTask task, String receiverName) {
        String structuredText = resolveStructuredText(task, receiverName);
        if (StringUtils.hasText(structuredText)) {
            return structuredText;
        }

        if (task.getTemplateId() == null) {
            return task.getContent();
        }
        MessageTemplate template = messageTemplateService.getTemplateById(task.getTemplateId());
        if (template == null) {
            return task.getContent();
        }
        return MessageTemplateUtil.renderTemplate(template.getTemplateContent(), receiverName);
    }

    public List<MassTaskMediaMaterial> resolveMediaMaterials(MassTask task,
                                                             String rawMaterial,
                                                             String receiverName,
                                                             String emptyMessage) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getMedia() != null) {
            return Collections.singletonList(MassTaskMediaMaterial.fromPayload(payload.getMedia()));
        }

        JSONObject json = parseMaterialObject(rawMaterial);
        if (json != null) {
            return Collections.singletonList(MassTaskMediaMaterial.fromJson(json));
        }

        JSONObject structuredObject = resolveStructuredTaskObject(task, receiverName);
        if (structuredObject != null) {
            JSONObject mediaJson = structuredObject.getJSONObject("media");
            if (mediaJson != null) {
                return Collections.singletonList(MassTaskMediaMaterial.fromJson(mediaJson));
            }

            List<MassTaskMediaMaterial> itemMaterials = resolveItemMaterials(structuredObject);
            if (!itemMaterials.isEmpty()) {
                return itemMaterials;
            }
        }

        throw new IllegalArgumentException(emptyMessage);
    }

    public MassTaskLinkPayload resolveLinkPayload(MassTask task, String receiverName) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getLink() != null) {
            return payload.getLink();
        }

        JSONObject json = resolveStructuredTaskObject(task, receiverName);
        if (json == null) {
            throw new IllegalArgumentException("link payload is empty");
        }

        JSONObject linkJson = json.getJSONObject("link");
        return (linkJson == null ? json : linkJson).toJavaObject(MassTaskLinkPayload.class);
    }

    public MassTaskAppMessageMaterial resolveAppMessageMaterial(MassTask task, String receiverName) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getApp() != null) {
            return MassTaskAppMessageMaterial.fromPayload(payload.getApp());
        }

        JSONObject json = resolveStructuredTaskObject(task, receiverName);
        if (json == null) {
            throw new IllegalArgumentException("app payload is empty");
        }

        JSONObject appJson = json.getJSONObject("app");
        return MassTaskAppMessageMaterial.fromJson(appJson == null ? json : appJson);
    }

    public CdnUploadResponse validateImageUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null
                || !StringUtils.hasText(uploadResponse.getCdn_key())
                || !StringUtils.hasText(uploadResponse.getAes_key())
                || !StringUtils.hasText(uploadResponse.getMd5())
                || uploadResponse.getSize() == null) {
            throw new IllegalStateException("image upload response is incomplete");
        }
        return uploadResponse;
    }

    public CdnUploadResponse validateFileUploadResponse(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null
                || !StringUtils.hasText(resolveCdnKey(uploadResponse))
                || !StringUtils.hasText(uploadResponse.getAes_key())
                || !StringUtils.hasText(uploadResponse.getMd5())
                || uploadResponse.getSize() == null) {
            throw new IllegalStateException("file upload response is incomplete");
        }
        return uploadResponse;
    }

    public CdnUploadResponse buildUploadResponseFromMaterial(MassTaskMediaMaterial material) {
        CdnUploadResponse uploadResponse = new CdnUploadResponse();
        uploadResponse.setCdn_key(resolveMaterialCdnKey(material));
        uploadResponse.setFileid(material.getFileId());
        uploadResponse.setAes_key(material.getAeskey());
        uploadResponse.setMd5(material.getMd5());
        uploadResponse.setSize(material.getFileSize());
        uploadResponse.setWidth(material.getWidth());
        uploadResponse.setHeight(material.getHeight());
        uploadResponse.setThumb_image_height(material.getThumbImageHeight());
        uploadResponse.setThumb_image_width(material.getThumbImageWidth());
        uploadResponse.setThumb_file_size(material.getThumbFileSize());
        uploadResponse.setThumb_file_md5(material.getThumbFileMd5());
        return validateFileUploadResponse(uploadResponse);
    }

    public String resolveCdnKey(CdnUploadResponse uploadResponse) {
        return StringUtils.hasText(uploadResponse.getCdn_key()) ? uploadResponse.getCdn_key() : uploadResponse.getFileid();
    }

    public String resolveMaterialCdnKey(MassTaskMediaMaterial material) {
        return StringUtils.hasText(material.getCdnkey()) ? material.getCdnkey() : material.getFileId();
    }

    public String resolveFileName(MassTaskMediaMaterial material) {
        return firstNonBlank(material.getFileName(), material.getFilename());
    }

    public String resolveStructuredText(MassTask task, String receiverName) {
        JSONObject structuredObject = resolveStructuredTaskObject(task, receiverName);
        if (structuredObject == null) {
            return null;
        }
        return firstNonBlank(
                structuredObject.getString("text"),
                structuredObject.getString("content")
        );
    }

    public JSONObject sendTextMessage(MassTaskReceiverContext receiverContext, String content) {
        SendTextRequest request = SendTextRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .content(content)
                .build();
        return postMessage("/wxwork/SendTextMsg", request);
    }

    public void ensureSuccess(JSONObject result, String action) {
        if (result == null) {
            throw new IllegalStateException(action + " response is empty");
        }

        Integer code = null;
        if (result.containsKey("errcode")) {
            code = result.getInteger("errcode");
        } else if (result.containsKey("code")) {
            code = result.getInteger("code");
        }
        if (code != null && code == 0) {
            return;
        }

        String message = firstNonBlank(result.getString("errmsg"), result.getString("msg"));
        throw new IllegalStateException(StringUtils.hasText(message) ? message : action + " failed");
    }

    public JSONObject successResult() {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("msg", "ok");
        return result;
    }

    public Integer resolveVoiceDuration(MassTaskMediaMaterial material) {
        if (material == null) {
            throw new IllegalArgumentException("voice material is empty");
        }
        if (material.getVoiceTime() != null) {
            return material.getVoiceTime();
        }

        byte[] audioBytes = loadSourceBytes(material, "voice");
        Integer inferredDuration = inferVoiceDuration(audioBytes);
        if (inferredDuration != null) {
            return inferredDuration;
        }
        throw new IllegalArgumentException("voice_time is required");
    }

    public Integer resolveVideoDuration(MassTaskMediaMaterial material) {
        if (material == null) {
            throw new IllegalArgumentException("video material is empty");
        }
        if (material.getVideoDuration() != null) {
            return material.getVideoDuration();
        }

        byte[] videoBytes = loadSourceBytes(material, "video");
        Integer inferredDuration = inferVideoDuration(videoBytes);
        if (inferredDuration != null) {
            return inferredDuration;
        }
        throw new IllegalArgumentException("video_duration is required");
    }

    public String requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public Integer requireInteger(Integer value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public Integer firstNonNull(Integer first, Integer second) {
        return first != null ? first : second;
    }

    public String firstNonBlank(String first, String second) {
        return StringUtils.hasText(first) ? first : second;
    }

    private MassTaskPayload resolveTaskPayload(MassTask task) {
        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
        return json == null ? null : json.toJavaObject(MassTaskPayload.class);
    }

    private JSONObject resolveStructuredTaskObject(MassTask task, String receiverName) {
        String renderedContent = resolveRenderedContentCandidate(task, receiverName);
        return firstStructuredObject(task.getPayloadJson(), renderedContent, task.getRemark());
    }

    private String resolveRenderedContentCandidate(MassTask task, String receiverName) {
        if (task == null) {
            return null;
        }
        if (task.getTemplateId() == null) {
            return task.getContent();
        }
        MessageTemplate template = messageTemplateService.getTemplateById(task.getTemplateId());
        if (template == null || !StringUtils.hasText(template.getTemplateContent())) {
            return task.getContent();
        }
        return MessageTemplateUtil.renderTemplate(template.getTemplateContent(), receiverName);
    }

    private List<MassTaskMediaMaterial> resolveItemMaterials(JSONObject json) {
        if (json == null) {
            return Collections.emptyList();
        }

        JSONArray items = json.getJSONArray("items");
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        List<MassTaskMediaMaterial> materials = new ArrayList<>(items.size());
        for (Object item : items) {
            if (item instanceof JSONObject) {
                materials.add(MassTaskMediaMaterial.fromJson((JSONObject) item));
                continue;
            }
            if (item != null) {
                materials.add(MassTaskMediaMaterial.fromJson(JSON.parseObject(JSON.toJSONString(item))));
            }
        }
        return materials;
    }

    private byte[] loadSourceBytes(MassTaskMediaMaterial material, String defaultFilenamePrefix) {
        if (material == null) {
            return null;
        }
        if (StringUtils.hasText(material.getBase64())) {
            return decodeBase64Bytes(material.getBase64());
        }
        if (StringUtils.hasText(material.getUrl())) {
            RemoteMediaResource resource = remoteMediaDownloadService.download(
                    material.getUrl(),
                    resolveFileName(material),
                    material.getContentType(),
                    defaultFilenamePrefix
            );
            return resource.getBytes();
        }
        return null;
    }

    private Integer inferVoiceDuration(byte[] audioBytes) {
        if (audioBytes == null || audioBytes.length == 0) {
            return null;
        }

        Integer wavDuration = inferWavDuration(audioBytes);
        if (wavDuration != null) {
            return wavDuration;
        }

        Integer mp3Duration = inferMp3Duration(audioBytes);
        if (mp3Duration != null) {
            return mp3Duration;
        }
        return null;
    }

    private Integer inferVideoDuration(byte[] videoBytes) {
        if (videoBytes == null || videoBytes.length == 0) {
            return null;
        }
        return inferMp4Duration(videoBytes);
    }

    private byte[] decodeBase64Bytes(String base64Payload) {
        String trimmed = base64Payload == null ? "" : base64Payload.trim();
        String encoded = trimmed;
        int commaIndex = trimmed.indexOf(',');
        if (trimmed.startsWith("data:") && commaIndex > 0) {
            encoded = trimmed.substring(commaIndex + 1);
        }

        String normalized = encoded.replaceAll("\\s", "");
        int remainder = normalized.length() % 4;
        if (remainder != 0) {
            StringBuilder builder = new StringBuilder(normalized);
            for (int i = remainder; i < 4; i++) {
                builder.append('=');
            }
            normalized = builder.toString();
        }

        try {
            return Base64.getDecoder().decode(normalized);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Integer inferWavDuration(byte[] bytes) {
        if (bytes.length < 44) {
            return null;
        }
        if (!startsWith(bytes, "RIFF") || !matchesAt(bytes, 8, "WAVE")) {
            return null;
        }

        int offset = 12;
        Integer dataSize = null;
        Integer byteRate = null;
        while (offset + 8 <= bytes.length) {
            String chunkId = new String(bytes, offset, 4, StandardCharsets.US_ASCII);
            int chunkSize = readLittleEndianInt(bytes, offset + 4);
            int nextOffset = offset + 8 + chunkSize + (chunkSize % 2);
            if (chunkSize < 0 || nextOffset > bytes.length + 1) {
                return null;
            }

            if ("fmt ".equals(chunkId) && chunkSize >= 16 && offset + 24 <= bytes.length) {
                byteRate = readLittleEndianInt(bytes, offset + 16);
            } else if ("data".equals(chunkId)) {
                dataSize = chunkSize;
            }

            if (dataSize != null && byteRate != null && byteRate > 0) {
                return Math.max(1, (int) Math.ceil((double) dataSize / (double) byteRate));
            }
            offset = nextOffset;
        }
        return null;
    }

    private Integer inferMp3Duration(byte[] bytes) {
        int offset = skipId3v2Tag(bytes);
        long totalSamples = 0L;
        int frameCount = 0;

        while (offset + 4 <= bytes.length) {
            int header = readBigEndianInt(bytes, offset);
            Mp3Frame frame = parseMp3Frame(header);
            if (frame == null) {
                offset++;
                continue;
            }

            if (frame.frameLength <= 0 || offset + frame.frameLength > bytes.length) {
                break;
            }

            totalSamples += frame.samplesPerFrame;
            frameCount++;
            offset += frame.frameLength;
        }

        if (frameCount == 0) {
            return null;
        }

        Mp3Frame firstFrame = findFirstMp3Frame(bytes, skipId3v2Tag(bytes));
        int sampleRate = firstFrame == null || firstFrame.sampleRate <= 0 ? 44100 : firstFrame.sampleRate;
        return Math.max(1, (int) Math.ceil((double) totalSamples / (double) sampleRate));
    }

    private Mp3Frame findFirstMp3Frame(byte[] bytes, int startOffset) {
        for (int offset = startOffset; offset + 4 <= bytes.length; offset++) {
            Mp3Frame frame = parseMp3Frame(readBigEndianInt(bytes, offset));
            if (frame != null) {
                return frame;
            }
        }
        return null;
    }

    private int skipId3v2Tag(byte[] bytes) {
        if (bytes.length < 10 || !startsWith(bytes, "ID3")) {
            return 0;
        }
        int size = ((bytes[6] & 0x7F) << 21)
                | ((bytes[7] & 0x7F) << 14)
                | ((bytes[8] & 0x7F) << 7)
                | (bytes[9] & 0x7F);
        int tagSize = 10 + size;
        return tagSize > bytes.length ? 0 : tagSize;
    }

    private Mp3Frame parseMp3Frame(int header) {
        if ((header & 0xFFE00000) != 0xFFE00000) {
            return null;
        }

        int versionBits = (header >>> 19) & 0x3;
        int layerBits = (header >>> 17) & 0x3;
        int bitrateIndex = (header >>> 12) & 0xF;
        int sampleRateIndex = (header >>> 10) & 0x3;
        int paddingBit = (header >>> 9) & 0x1;

        if (versionBits == 1 || layerBits != 1 || bitrateIndex == 0 || bitrateIndex == 15 || sampleRateIndex == 3) {
            return null;
        }

        int sampleRate = resolveMp3SampleRate(versionBits, sampleRateIndex);
        int bitrate = resolveMp3Bitrate(versionBits, bitrateIndex);
        if (sampleRate <= 0 || bitrate <= 0) {
            return null;
        }

        int frameLength;
        int samplesPerFrame;
        if (versionBits == 3) {
            frameLength = (144 * bitrate * 1000) / sampleRate + paddingBit;
            samplesPerFrame = 1152;
        } else {
            frameLength = (72 * bitrate * 1000) / sampleRate + paddingBit;
            samplesPerFrame = 576;
        }

        if (frameLength <= 4) {
            return null;
        }
        return new Mp3Frame(frameLength, sampleRate, samplesPerFrame);
    }

    private int resolveMp3SampleRate(int versionBits, int sampleRateIndex) {
        int[] baseRates = new int[]{44100, 48000, 32000};
        int sampleRate = baseRates[sampleRateIndex];
        if (versionBits == 2) {
            return sampleRate / 2;
        }
        if (versionBits == 0) {
            return sampleRate / 4;
        }
        return sampleRate;
    }

    private int resolveMp3Bitrate(int versionBits, int bitrateIndex) {
        int[] mpeg1Layer3 = new int[]{0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 0};
        int[] mpeg2Layer3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, 0};
        return versionBits == 3 ? mpeg1Layer3[bitrateIndex] : mpeg2Layer3[bitrateIndex];
    }

    private Integer inferMp4Duration(byte[] bytes) {
        int offset = 0;
        while (offset + 8 <= bytes.length) {
            Mp4Box box = readMp4Box(bytes, offset, bytes.length);
            if (box == null) {
                return null;
            }
            if ("moov".equals(box.type)) {
                return extractMp4DurationFromMoov(bytes, box.contentOffset, box.endOffset);
            }
            offset = box.endOffset;
        }
        return null;
    }

    private Integer extractMp4DurationFromMoov(byte[] bytes, int startOffset, int endOffset) {
        int offset = startOffset;
        while (offset + 8 <= endOffset) {
            Mp4Box box = readMp4Box(bytes, offset, endOffset);
            if (box == null) {
                return null;
            }
            if ("mvhd".equals(box.type)) {
                return parseMvhdDuration(bytes, box.contentOffset, box.endOffset);
            }
            offset = box.endOffset;
        }
        return null;
    }

    private Integer parseMvhdDuration(byte[] bytes, int contentOffset, int endOffset) {
        if (contentOffset + 20 > endOffset || contentOffset >= bytes.length) {
            return null;
        }

        int version = bytes[contentOffset] & 0xFF;
        long timescale;
        long duration;
        if (version == 1) {
            if (contentOffset + 32 > endOffset) {
                return null;
            }
            timescale = readUnsignedInt(bytes, contentOffset + 20);
            duration = readLong(bytes, contentOffset + 24);
        } else {
            timescale = readUnsignedInt(bytes, contentOffset + 12);
            duration = readUnsignedInt(bytes, contentOffset + 16);
        }

        if (timescale <= 0 || duration <= 0) {
            return null;
        }
        return Math.max(1, (int) Math.ceil((double) duration / (double) timescale));
    }

    private Mp4Box readMp4Box(byte[] bytes, int offset, int limit) {
        if (offset < 0 || offset + 8 > limit || offset + 8 > bytes.length) {
            return null;
        }

        long boxSize = readUnsignedInt(bytes, offset);
        String boxType = new String(bytes, offset + 4, 4, StandardCharsets.US_ASCII);
        int headerSize = 8;
        if (boxSize == 1L) {
            long extendedSize = readLong(bytes, offset + 8);
            if (extendedSize < 16L) {
                return null;
            }
            boxSize = extendedSize;
            headerSize = 16;
        } else if (boxSize == 0L) {
            boxSize = limit - offset;
        }

        if (boxSize < headerSize) {
            return null;
        }
        long endOffset = offset + boxSize;
        if (endOffset > limit || endOffset > bytes.length) {
            return null;
        }
        return new Mp4Box(boxType, offset + headerSize, (int) endOffset);
    }

    private boolean startsWith(byte[] bytes, String value) {
        return matchesAt(bytes, 0, value);
    }

    private boolean matchesAt(byte[] bytes, int offset, String value) {
        byte[] ascii = value.getBytes(StandardCharsets.US_ASCII);
        if (offset < 0 || bytes.length < offset + ascii.length) {
            return false;
        }
        for (int i = 0; i < ascii.length; i++) {
            if (bytes[offset + i] != ascii[i]) {
                return false;
            }
        }
        return true;
    }

    private int readLittleEndianInt(byte[] bytes, int offset) {
        if (offset + 4 > bytes.length) {
            return -1;
        }
        return (bytes[offset] & 0xFF)
                | ((bytes[offset + 1] & 0xFF) << 8)
                | ((bytes[offset + 2] & 0xFF) << 16)
                | ((bytes[offset + 3] & 0xFF) << 24);
    }

    private int readBigEndianInt(byte[] bytes, int offset) {
        if (offset + 4 > bytes.length) {
            return -1;
        }
        return ((bytes[offset] & 0xFF) << 24)
                | ((bytes[offset + 1] & 0xFF) << 16)
                | ((bytes[offset + 2] & 0xFF) << 8)
                | (bytes[offset + 3] & 0xFF);
    }

    private long readUnsignedInt(byte[] bytes, int offset) {
        if (offset + 4 > bytes.length) {
            return -1L;
        }
        return ((long) (bytes[offset] & 0xFF) << 24)
                | ((long) (bytes[offset + 1] & 0xFF) << 16)
                | ((long) (bytes[offset + 2] & 0xFF) << 8)
                | (bytes[offset + 3] & 0xFF);
    }

    private long readLong(byte[] bytes, int offset) {
        if (offset + 8 > bytes.length) {
            return -1L;
        }
        return ((long) (bytes[offset] & 0xFF) << 56)
                | ((long) (bytes[offset + 1] & 0xFF) << 48)
                | ((long) (bytes[offset + 2] & 0xFF) << 40)
                | ((long) (bytes[offset + 3] & 0xFF) << 32)
                | ((long) (bytes[offset + 4] & 0xFF) << 24)
                | ((long) (bytes[offset + 5] & 0xFF) << 16)
                | ((long) (bytes[offset + 6] & 0xFF) << 8)
                | (bytes[offset + 7] & 0xFF);
    }

    private static class Mp3Frame {
        private final int frameLength;
        private final int sampleRate;
        private final int samplesPerFrame;

        private Mp3Frame(int frameLength, int sampleRate, int samplesPerFrame) {
            this.frameLength = frameLength;
            this.sampleRate = sampleRate;
            this.samplesPerFrame = samplesPerFrame;
        }
    }

    private static class Mp4Box {
        private final String type;
        private final int contentOffset;
        private final int endOffset;

        private Mp4Box(String type, int contentOffset, int endOffset) {
            this.type = type;
            this.contentOffset = contentOffset;
            this.endOffset = endOffset;
        }
    }

    private JSONObject parseMaterialObject(String rawMaterial) {
        if (!StringUtils.hasText(rawMaterial)) {
            return null;
        }

        String trimmed = rawMaterial.trim();
        if (trimmed.startsWith("{")) {
            return JSON.parseObject(trimmed);
        }

        JSONObject json = new JSONObject();
        if (isUrl(trimmed)) {
            json.put("url", trimmed);
        } else if (isDataUrl(trimmed)) {
            json.put("base64", trimmed);
        } else {
            json.put("cdnkey", trimmed);
        }
        return json;
    }

    private JSONObject firstStructuredObject(String... candidates) {
        if (candidates == null) {
            return null;
        }

        for (String candidate : candidates) {
            if (!StringUtils.hasText(candidate)) {
                continue;
            }
            String trimmed = candidate.trim();
            if (!trimmed.startsWith("{")) {
                continue;
            }
            return JSON.parseObject(trimmed);
        }
        return null;
    }

    private boolean isUrl(String value) {
        return value.startsWith("http://") || value.startsWith("https://");
    }

    private boolean isDataUrl(String value) {
        return value.startsWith("data:") || value.contains(";base64,");
    }
}
