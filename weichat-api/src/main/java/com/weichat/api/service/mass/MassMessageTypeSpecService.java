package com.weichat.api.service.mass;

import com.weichat.api.support.mass.MassMessageType;
import com.weichat.api.vo.response.mass.MassMessageFieldSpecResponse;
import com.weichat.api.vo.response.mass.MassMessageTypeSpecResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MassMessageTypeSpecService {

    public List<MassMessageTypeSpecResponse> getAllSpecs() {
        List<MassMessageTypeSpecResponse> specs = new ArrayList<>();
        specs.add(buildTextSpec());
        specs.add(buildImageSpec());
        specs.add(buildFileSpec());
        specs.add(buildVoiceSpec());
        specs.add(buildVideoSpec());
        specs.add(buildLinkSpec());
        specs.add(buildAppSpec());
        return specs;
    }

    private MassMessageTypeSpecResponse buildTextSpec() {
        return buildSpec(
                MassMessageType.TEXT,
                "text",
                true,
                true,
                Collections.singletonList(field("payload.content", "Content", "Required for text message", "string", true)),
                Collections.emptyList(),
                Arrays.asList("Maps to /wxwork/SendTextMsg", "Current task execution only fully supports text")
        );
    }

    private MassMessageTypeSpecResponse buildImageSpec() {
        return buildSpec(
                MassMessageType.IMAGE,
                "image",
                true,
                false,
                Arrays.asList(
                        field("payload.media", "Media", "Provide url or base64", "object", true),
                        field("payload.media.url|payload.media.base64", "Source", "Either url or base64", "string", true)
                ),
                Arrays.asList(
                        field("payload.media.filename", "Filename", "Recommended for base64 upload", "string", false),
                        field("payload.media.contentType", "Content type", "Example: image/jpeg", "string", false),
                        field("payload.media.isHd", "HD flag", "Used when forwarding received image", "integer", false)
                ),
                Arrays.asList(
                        "Upload via CdnImageService before send to get cdnkey/aeskey/md5/fileSize",
                        "Current task execution does not route to /wxwork/SendCDNImgMsg yet"
                )
        );
    }

    private MassMessageTypeSpecResponse buildFileSpec() {
        return buildSpec(
                MassMessageType.FILE,
                "file",
                true,
                false,
                Arrays.asList(
                        field("payload.media", "Media", "Provide url or base64", "object", true),
                        field("payload.media.url|payload.media.base64", "Source", "Either url or base64", "string", true)
                ),
                Arrays.asList(
                        field("payload.media.filename", "Filename", "Use the real filename when possible", "string", false),
                        field("payload.media.contentType", "Content type", "Example: application/pdf", "string", false)
                ),
                Arrays.asList(
                        "Upload via CdnFileService before send to get cdnkey/aeskey/md5/fileSize",
                        "Current task execution does not route to file send API yet"
                )
        );
    }

    private MassMessageTypeSpecResponse buildVoiceSpec() {
        return buildSpec(
                MassMessageType.VOICE,
                "voice",
                true,
                false,
                Arrays.asList(
                        field("payload.media", "Media", "Provide url or base64", "object", true),
                        field("payload.media.url|payload.media.base64", "Source", "Either url or base64", "string", true)
                ),
                Arrays.asList(
                        field("payload.media.filename", "Filename", "Example: demo.mp3", "string", false),
                        field("payload.media.contentType", "Content type", "Example: audio/mpeg", "string", false),
                        field("payload.media.voiceTime", "Voice time", "Seconds. Backend may infer it if omitted", "integer", false)
                ),
                Arrays.asList(
                        "Upload via CdnFileService before send to get cdnkey/aeskey/md5/fileSize",
                        "Maps to /wxwork/SendCDNVoiceMsg",
                        "Current task execution does not route to voice send API yet"
                )
        );
    }

    private MassMessageTypeSpecResponse buildVideoSpec() {
        return buildSpec(
                MassMessageType.VIDEO,
                "video",
                true,
                false,
                Arrays.asList(
                        field("payload.media", "Media", "Provide url or base64", "object", true),
                        field("payload.media.url|payload.media.base64", "Source", "Either url or base64", "string", true)
                ),
                Arrays.asList(
                        field("payload.media.filename", "Filename", "Use the real video filename", "string", false),
                        field("payload.media.contentType", "Content type", "Example: video/mp4", "string", false)
                ),
                Arrays.asList(
                        "Upload via CdnFileService before send to get cdnkey/aeskey/md5/fileSize",
                        "Current task execution does not route to video send API yet"
                )
        );
    }

    private MassMessageTypeSpecResponse buildLinkSpec() {
        return buildSpec(
                MassMessageType.LINK,
                "link",
                false,
                false,
                Arrays.asList(
                        field("payload.link.url", "URL", "Maps to downstream url", "string", true),
                        field("payload.link.title", "Title", "Maps to downstream title", "string", true),
                        field("payload.link.content", "Content", "Maps to downstream content", "string", true)
                ),
                Collections.singletonList(field("payload.link.imgurl", "Image URL", "Maps to downstream imgurl", "string", false)),
                Arrays.asList(
                        "Maps to /wxwork/SendLinkMsg",
                        "Current mass_task table does not persist link card fields yet"
                )
        );
    }

    private MassMessageTypeSpecResponse buildAppSpec() {
        return buildSpec(
                MassMessageType.APP,
                "app",
                false,
                false,
                Arrays.asList(
                        field("payload.app.desc", "Desc", "Maps to downstream desc", "string", true),
                        field("payload.app.appName", "App name", "Maps to downstream appName", "string", true),
                        field("payload.app.title", "Title", "Maps to downstream title", "string", true),
                        field("payload.app.pagepath", "Page path", "Maps to downstream pagepath", "string", true),
                        field("payload.app.username", "Username", "Maps to downstream username", "string", true),
                        field("payload.app.appid", "Appid", "Maps to downstream appid", "string", true),
                        field("payload.app.cover", "Cover media", "Provide url or base64", "object", true),
                        field("payload.app.cover.url|payload.app.cover.base64", "Cover source", "Either url or base64", "string", true)
                ),
                Collections.singletonList(field("payload.app.weappIconUrl", "Icon URL", "Maps to downstream weappIconUrl", "string", false)),
                Arrays.asList(
                        "Maps to /wxwork/SendAppMsg",
                        "Upload cover via CdnFileService before send to get cdnkey/aeskey/md5/fileSize",
                        "Current mass_task table does not persist mini program fields yet"
                )
        );
    }

    private MassMessageTypeSpecResponse buildSpec(Integer msgType,
                                                  String code,
                                                  boolean storageSupported,
                                                  boolean executionSupported,
                                                  List<MassMessageFieldSpecResponse> requiredFields,
                                                  List<MassMessageFieldSpecResponse> optionalFields,
                                                  List<String> notes) {
        MassMessageTypeSpecResponse response = new MassMessageTypeSpecResponse();
        response.setMsgType(msgType);
        response.setCode(code);
        response.setLabel(MassMessageType.labelOf(msgType));
        response.setStorageSupported(storageSupported);
        response.setExecutionSupported(executionSupported);
        response.setRequiredFields(requiredFields);
        response.setOptionalFields(optionalFields);
        response.setNotes(notes);
        return response;
    }

    private MassMessageFieldSpecResponse field(String field,
                                               String label,
                                               String description,
                                               String fieldType,
                                               boolean required) {
        return new MassMessageFieldSpecResponse(field, label, description, fieldType, required);
    }
}
