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

/**
 * 群发任务消息支持服务
 * <p>
 * 提供群发消息处理的核心支持功能，包括：
 * <ul>
 *   <li>消息内容解析与模板渲染</li>
 *   <li>媒体素材处理（图片、视频、语音、文件）</li>
 *   <li>CDN上传响应验证与转换</li>
 *   <li>媒体文件时长推断（WAV、MP3、MP4格式）</li>
 *   <li>Base64编码解码与数据URL处理</li>
 *   <li>企业微信API消息发送</li>
 *   <li>响应结果校验与异常处理</li>
 * </ul>
 * </p>
 *
 * @author weichat
 * @since 1.0
 */
@Service
public class MassTaskMessageSupport {

    /**
     * 企业微信API客户端，用于调用企业微信接口
     */
    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    /**
     * 消息模板服务，用于获取和渲染消息模板
     */
    @Autowired
    private MessageTemplateService messageTemplateService;

    /**
     * 远程媒体下载服务，用于从URL下载媒体文件
     */
    @Autowired
    private RemoteMediaDownloadService remoteMediaDownloadService;

    /**
     * 发送POST请求到企业微信API
     *
     * @param endpoint API端点路径
     * @param request  请求对象
     * @return API响应JSON对象
     */
    public JSONObject postMessage(String endpoint, Object request) {
        return wxWorkApiClient.post(endpoint, JSON.parseObject(JSON.toJSONString(request)));
    }

    /**
     * 解析群发任务的消息内容
     * <p>
     * 优先解析结构化文本，若不存在则尝试使用模板渲染，最后返回原始内容。
     * </p>
     *
     * @param task         群发任务对象
     * @param receiverName 接收者姓名，用于模板变量替换
     * @return 解析后的消息内容
     */
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

    /**
     * 解析群发任务的媒体素材列表
     * <p>
     * 按优先级依次尝试：
     * 1. 从任务载荷中的media字段解析
     * 2. 从原始素材字符串解析
     * 3. 从结构化任务对象中的media或items字段解析
     * </p>
     *
     * @param task         群发任务对象
     * @param rawMaterial  原始素材字符串（可能是JSON、URL或CDN key）
     * @param receiverName 接收者姓名，用于模板渲染
     * @param emptyMessage 素材为空时的异常提示信息
     * @return 媒体素材列表
     * @throws IllegalArgumentException 当所有解析方式都失败时抛出
     */
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

    /**
     * 解析群发任务的链接载荷
     * <p>
     * 优先从任务载荷中获取，若不存在则从结构化任务对象中解析。
     * </p>
     *
     * @param task         群发任务对象
     * @param receiverName 接收者姓名，用于模板渲染
     * @return 链接载荷对象
     * @throws IllegalArgumentException 当链接载荷为空时抛出
     */
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

    /**
     * 解析群发任务的小程序消息素材
     * <p>
     * 优先从任务载荷中获取，若不存在则从结构化任务对象中解析。
     * </p>
     *
     * @param task         群发任务对象
     * @param receiverName 接收者姓名，用于模板渲染
     * @return 小程序消息素材对象
     * @throws IllegalArgumentException 当小程序载荷为空时抛出
     */
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

    /**
     * 验证图片上传响应的完整性
     * <p>
     * 检查CDN key、AES key、MD5和文件大小是否都存在。
     * </p>
     *
     * @param uploadResponse CDN上传响应对象
     * @return 验证通过的上传响应对象
     * @throws IllegalStateException 当响应不完整时抛出
     */
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

    /**
     * 验证文件上传响应的完整性
     * <p>
     * 检查CDN key（或file_id）、AES key、MD5和文件大小是否都存在。
     * </p>
     *
     * @param uploadResponse CDN上传响应对象
     * @return 验证通过的上传响应对象
     * @throws IllegalStateException 当响应不完整时抛出
     */
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

    /**
     * 从媒体素材构建CDN上传响应对象
     * <p>
     * 将已上传的媒体素材信息转换为CDN上传响应格式，并进行完整性验证。
     * </p>
     *
     * @param material 媒体素材对象
     * @return CDN上传响应对象
     * @throws IllegalStateException 当响应不完整时抛出
     */
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

    /**
     * 解析CDN上传响应中的CDN key
     * <p>
     * 优先返回cdn_key字段，若为空则返回fileid字段。
     * </p>
     *
     * @param uploadResponse CDN上传响应对象
     * @return CDN key或file_id
     */
    public String resolveCdnKey(CdnUploadResponse uploadResponse) {
        return StringUtils.hasText(uploadResponse.getCdn_key()) ? uploadResponse.getCdn_key() : uploadResponse.getFileid();
    }

    /**
     * 解析媒体素材中的CDN key
     * <p>
     * 优先返回cdnkey字段，若为空则返回fileId字段。
     * </p>
     *
     * @param material 媒体素材对象
     * @return CDN key或file_id
     */
    public String resolveMaterialCdnKey(MassTaskMediaMaterial material) {
        return StringUtils.hasText(material.getCdnkey()) ? material.getCdnkey() : material.getFileId();
    }

    /**
     * 解析媒体素材的文件名
     * <p>
     * 优先返回fileName字段，若为空则返回filename字段。
     * </p>
     *
     * @param material 媒体素材对象
     * @return 文件名
     */
    public String resolveFileName(MassTaskMediaMaterial material) {
        return firstNonBlank(material.getFileName(), material.getFilename());
    }

    /**
     * 解析结构化任务对象中的文本内容
     * <p>
     * 从结构化JSON对象中提取text或content字段。
     * </p>
     *
     * @param task         群发任务对象
     * @param receiverName 接收者姓名，用于模板渲染
     * @return 文本内容，若不存在则返回null
     */
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

    /**
     * 发送文本消息
     *
     * @param receiverContext 接收者上下文信息
     * @param content         消息文本内容
     * @return API响应JSON对象
     */
    public JSONObject sendTextMessage(MassTaskReceiverContext receiverContext, String content) {
        SendTextRequest request = SendTextRequest.builder()
                .uuid(receiverContext.getSenderUuid())
                .send_userid(receiverContext.getReceiverUserId())
                .isRoom(receiverContext.isRoomMessage())
                .content(content)
                .build();
        return postMessage("/wxwork/SendTextMsg", request);
    }

    /**
     * 确保API响应成功
     * <p>
     * 检查响应中的errcode或code字段，若不为0则抛出异常。
     * </p>
     *
     * @param result API响应JSON对象
     * @param action 操作描述，用于异常信息
     * @throws IllegalStateException 当响应为空或返回错误码时抛出
     */
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

    /**
     * 创建成功响应对象
     *
     * @return 包含code=0和msg=ok的JSON对象
     */
    public JSONObject successResult() {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("msg", "ok");
        return result;
    }

    /**
     * 解析语音时长
     * <p>
     * 从CDN上传响应中获取视频时长字段（实际用于语音时长）。
     * </p>
     *
     * @param uploadResponse CDN上传响应对象
     * @return 语音时长（秒）
     */
    public Integer resolveVoiceDuration(CdnUploadResponse uploadResponse) {
        return uploadResponse.getVideoDuration();
    }

    /**
     * 要求字符串值非空
     *
     * @param value   待检查的字符串值
     * @param message 为空时的异常信息
     * @return 非空的字符串值
     * @throws IllegalArgumentException 当值为空时抛出
     */
    public String requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * 要求整数值非null
     *
     * @param value   待检查的整数值
     * @param message 为null时的异常信息
     * @return 非null的整数值
     * @throws IllegalArgumentException 当值为null时抛出
     */
    public Integer requireInteger(Integer value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * 返回第一个非null的整数值
     *
     * @param first  第一个候选值
     * @param second 第二个候选值
     * @return 第一个非null的值，若都为null则返回null
     */
    public Integer firstNonNull(Integer first, Integer second) {
        return first != null ? first : second;
    }

    /**
     * 返回第一个非空的字符串值
     *
     * @param first  第一个候选值
     * @param second 第二个候选值
     * @return 第一个非空的值，若都为空则返回第二个值
     */
    public String firstNonBlank(String first, String second) {
        return StringUtils.hasText(first) ? first : second;
    }

    /**
     * 解析群发任务的载荷对象
     * <p>
     * 按优先级从payloadJson、content、remark字段中查找并解析JSON对象。
     * </p>
     *
     * @param task 群发任务对象
     * @return 任务载荷对象，若无法解析则返回null
     */
    private MassTaskPayload resolveTaskPayload(MassTask task) {
        JSONObject json = firstStructuredObject(task.getPayloadJson(), task.getContent(), task.getRemark());
        return json == null ? null : json.toJavaObject(MassTaskPayload.class);
    }

    /**
     * 解析结构化任务对象
     * <p>
     * 先渲染模板内容，再按优先级从payloadJson、渲染后内容、remark字段中查找并解析JSON对象。
     * </p>
     *
     * @param task         群发任务对象
     * @param receiverName 接收者姓名，用于模板渲染
     * @return 结构化JSON对象，若无法解析则返回null
     */
    private JSONObject resolveStructuredTaskObject(MassTask task, String receiverName) {
        String renderedContent = resolveRenderedContentCandidate(task, receiverName);
        return firstStructuredObject(task.getPayloadJson(), renderedContent, task.getRemark());
    }

    /**
     * 解析渲染后的内容候选值
     * <p>
     * 若任务关联了模板，则渲染模板内容；否则返回原始内容。
     * </p>
     *
     * @param task         群发任务对象
     * @param receiverName 接收者姓名，用于模板变量替换
     * @return 渲染后的内容
     */
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

    /**
     * 从结构化对象中解析媒体素材列表
     * <p>
     * 从JSON对象的items数组中提取多个媒体素材。
     * </p>
     *
     * @param json 结构化JSON对象
     * @return 媒体素材列表，若items不存在或为空则返回空列表
     */
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

    /**
     * 解析原始素材字符串为JSON对象
     * <p>
     * 支持三种格式：
     * 1. JSON对象字符串（以"{"开头）
     * 2. HTTP/HTTPS URL
     * 3. Data URL（Base64编码）
     * 4. CDN key（其他情况）
     * </p>
     *
     * @param rawMaterial 原始素材字符串
     * @return JSON对象，若为空则返回null
     */
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

    /**
     * 从候选字符串中查找第一个结构化JSON对象
     * <p>
     * 按顺序遍历候选字符串，返回第一个以"{"开头的有效JSON对象。
     * </p>
     *
     * @param candidates 候选字符串数组
     * @return 第一个有效的JSON对象，若都无效则返回null
     */
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

    /**
     * 判断字符串是否为HTTP/HTTPS URL
     *
     * @param value 待判断的字符串
     * @return true-是URL，false-不是URL
     */
    private boolean isUrl(String value) {
        return value.startsWith("http://") || value.startsWith("https://");
    }

    /**
     * 判断字符串是否为Data URL（Base64编码）
     *
     * @param value 待判断的字符串
     * @return true-是Data URL，false-不是Data URL
     */
    private boolean isDataUrl(String value) {
        return value.startsWith("data:") || value.contains(";base64,");
    }
}
