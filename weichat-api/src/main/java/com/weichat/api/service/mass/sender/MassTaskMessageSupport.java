package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.util.MessageTemplateUtil;
import com.weichat.api.vo.request.mass.MassTaskAppPayload;
import com.weichat.api.vo.request.mass.MassTaskLinkPayload;
import com.weichat.api.vo.request.mass.MassTaskPayload;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MessageTemplate;
import com.weichat.common.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MassTaskMessageSupport {

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private MessageTemplateService messageTemplateService;

    public JSONObject postMessage(String endpoint, Object request) {
        return wxWorkApiClient.post(endpoint, JSON.parseObject(JSON.toJSONString(request)));
    }

    public String resolveContent(MassTask task, String receiverName) {
        if (task.getTemplateId() == null) {
            return task.getContent();
        }
        MessageTemplate template = messageTemplateService.getTemplateById(task.getTemplateId());
        if (template == null) {
            return task.getContent();
        }
        return MessageTemplateUtil.renderTemplate(template.getTemplateContent(), receiverName);
    }

    public MassTaskMediaMaterial resolveMediaMaterial(MassTask task, String rawMaterial, String emptyMessage) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getMedia() != null) {
            return MassTaskMediaMaterial.fromPayload(payload.getMedia());
        }

        JSONObject json = parseMaterialObject(rawMaterial);
        if (json == null) {
            throw new IllegalArgumentException(emptyMessage);
        }
        return MassTaskMediaMaterial.fromJson(json);
    }

    public MassTaskLinkPayload resolveLinkPayload(MassTask task) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getLink() != null) {
            return payload.getLink();
        }

        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
        if (json == null) {
            throw new IllegalArgumentException("link payload is empty");
        }

        JSONObject linkJson = json.getJSONObject("link");
        return (linkJson == null ? json : linkJson).toJavaObject(MassTaskLinkPayload.class);
    }

    public MassTaskAppMessageMaterial resolveAppMessageMaterial(MassTask task) {
        MassTaskPayload payload = resolveTaskPayload(task);
        if (payload != null && payload.getApp() != null) {
            return MassTaskAppMessageMaterial.fromPayload(payload.getApp());
        }

        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
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
