package com.weichat.api.service.mass;

import com.weichat.api.support.mass.MassMessageType;
import com.weichat.api.vo.request.mass.MassTaskAppPayload;
import com.weichat.api.vo.request.mass.MassTaskCreateRequest;
import com.weichat.api.vo.request.mass.MassTaskLinkPayload;
import com.weichat.api.vo.request.mass.MassTaskMediaPayload;
import com.weichat.api.vo.request.mass.MassTaskPayload;
import com.weichat.common.entity.MassTask;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Central validator for mass task create payloads.
 */
@Service
public class MassTaskRequestValidator {

    public List<String> validateCreateRequest(MassTaskCreateRequest request) {
        List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("request is required");
            return errors;
        }
        validateCommon(request.getTaskType(), request.getReceiverType(), request.getReceiverIds(), request.getMsgType(), errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        MassTaskPayload payload = request.getPayload();
        if (payload == null) {
            errors.add("payload is required");
            return errors;
        }

        switch (request.getMsgType()) {
            case MassMessageType.TEXT:
                if (!StringUtils.hasText(payload.getContent()) && request.getTemplateId() == null) {
                    errors.add("payload.content is required when msgType=text and templateId is empty");
                }
                break;
            case MassMessageType.IMAGE:
            case MassMessageType.FILE:
            case MassMessageType.VOICE:
            case MassMessageType.VIDEO:
                validateMediaPayload(payload.getMedia(), errors, "payload.media");
                break;
            case MassMessageType.LINK:
                validateLinkPayload(payload.getLink(), errors);
                break;
            case MassMessageType.APP:
                validateAppPayload(payload.getApp(), errors);
                break;
            default:
                errors.add("unsupported msgType: " + request.getMsgType());
                break;
        }
        return errors;
    }

    public List<String> validateLegacyTask(MassTask massTask, List<Long> receiverIds, Integer receiverType) {
        List<String> errors = new ArrayList<>();
        if (massTask == null) {
            errors.add("massTask is required");
            return errors;
        }
        validateCommon(massTask.getTaskType(), receiverType, receiverIds, massTask.getMsgType(), errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        switch (massTask.getMsgType()) {
            case MassMessageType.TEXT:
                if (!StringUtils.hasText(massTask.getContent()) && massTask.getTemplateId() == null) {
                    errors.add("content is required when msgType=text and templateId is empty");
                }
                break;
            case MassMessageType.IMAGE:
                if (!StringUtils.hasText(massTask.getImageMediaId())) {
                    errors.add("imageMediaId is required when msgType=image");
                }
                break;
            case MassMessageType.FILE:
                if (!StringUtils.hasText(massTask.getFileMediaId())) {
                    errors.add("fileMediaId is required when msgType=file");
                }
                break;
            case MassMessageType.VOICE:
                if (!StringUtils.hasText(massTask.getAudioMediaId())) {
                    errors.add("audioMediaId is required when msgType=voice");
                }
                break;
            case MassMessageType.VIDEO:
                if (!StringUtils.hasText(massTask.getVideoMediaId())) {
                    errors.add("videoMediaId is required when msgType=video");
                }
                break;
            case MassMessageType.LINK:
                errors.add("legacy /api/v1/mass/task does not support msgType=link, use /api/v1/mass/message-type-specs and /api/v1/mass/task/validate");
                break;
            case MassMessageType.APP:
                errors.add("legacy /api/v1/mass/task does not support msgType=app, use /api/v1/mass/message-type-specs and /api/v1/mass/task/validate");
                break;
            default:
                errors.add("unsupported msgType: " + massTask.getMsgType());
                break;
        }
        return errors;
    }

    private void validateCommon(Integer taskType,
                                Integer receiverType,
                                List<Long> receiverIds,
                                Integer msgType,
                                List<String> errors) {
        if (taskType == null || (taskType != 1 && taskType != 2)) {
            errors.add("taskType must be 1 or 2");
        }
        if (receiverType == null || (receiverType != 1 && receiverType != 2)) {
            errors.add("receiverType must be 1 or 2");
        }
        if (taskType != null && receiverType != null && !taskType.equals(receiverType)) {
            errors.add("taskType and receiverType must match");
        }
        if (CollectionUtils.isEmpty(receiverIds)) {
            errors.add("receiverIds must not be empty");
        }
        if (!MassMessageType.contains(msgType)) {
            errors.add("msgType is invalid");
        }
    }

    private void validateMediaPayload(MassTaskMediaPayload mediaPayload, List<String> errors, String fieldPrefix) {
        if (mediaPayload == null) {
            errors.add(fieldPrefix + " is required");
            return;
        }
        if (!StringUtils.hasText(mediaPayload.getUrl()) && !StringUtils.hasText(mediaPayload.getBase64())) {
            errors.add(fieldPrefix + ".url or " + fieldPrefix + ".base64 is required");
        }
    }

    private void validateLinkPayload(MassTaskLinkPayload linkPayload, List<String> errors) {
        if (linkPayload == null) {
            errors.add("payload.link is required");
            return;
        }
        if (!StringUtils.hasText(linkPayload.getUrl())) {
            errors.add("payload.link.url is required");
        }
        if (!StringUtils.hasText(linkPayload.getTitle())) {
            errors.add("payload.link.title is required");
        }
        if (!StringUtils.hasText(linkPayload.getContent())) {
            errors.add("payload.link.content is required");
        }
    }

    private void validateAppPayload(MassTaskAppPayload appPayload, List<String> errors) {
        if (appPayload == null) {
            errors.add("payload.app is required");
            return;
        }
        if (!StringUtils.hasText(appPayload.getDesc())) {
            errors.add("payload.app.desc is required");
        }
        if (!StringUtils.hasText(appPayload.getAppName())) {
            errors.add("payload.app.appName is required");
        }
        if (!StringUtils.hasText(appPayload.getTitle())) {
            errors.add("payload.app.title is required");
        }
        if (!StringUtils.hasText(appPayload.getPagepath())) {
            errors.add("payload.app.pagepath is required");
        }
        if (!StringUtils.hasText(appPayload.getUsername())) {
            errors.add("payload.app.username is required");
        }
        if (!StringUtils.hasText(appPayload.getAppid())) {
            errors.add("payload.app.appid is required");
        }
        validateMediaPayload(appPayload.getCover(), errors, "payload.app.cover");
    }
}
