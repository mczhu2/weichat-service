package com.weichat.api.testsupport;

import com.alibaba.fastjson.JSON;
import com.weichat.api.service.DownstreamMessageContentService;
import com.weichat.api.vo.callback.DownstreamCallbackPayload;
import com.weichat.api.vo.callback.DownstreamMediaVo;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxMessageInfoService;
import com.weichat.common.service.WxUserInfoService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DownstreamMessageContentManualRunner {

    private static final long DEFAULT_IMAGE_ID = 510L;
    private static final long DEFAULT_VIDEO_ID = 512L;

    public static void main(String[] args) {
        List<Long> messageIds = resolveMessageIds(args);
        ConfigurableApplicationContext context = new SpringApplicationBuilder(com.weichat.api.ApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        try {
            WxMessageInfoService wxMessageInfoService = context.getBean(WxMessageInfoService.class);
            WxUserInfoService wxUserInfoService = context.getBean(WxUserInfoService.class);
            DownstreamMessageContentService downstreamMessageContentService =
                    context.getBean(DownstreamMessageContentService.class);

            for (Long messageId : messageIds) {
                WxMessageInfo wxMessageInfo = wxMessageInfoService.selectByPrimaryKey(messageId);
                if (wxMessageInfo == null) {
                    throw new IllegalStateException("wxMessageInfo id=" + messageId + " not found");
                }

                WxUserInfo receiverUser = wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver());
                if (receiverUser == null || !StringUtils.hasText(receiverUser.getUuid())) {
                    throw new IllegalStateException("receiver uuid missing for message id=" + messageId);
                }

                DownstreamCallbackPayload payload = downstreamMessageContentService.resolveCallbackPayload(
                        wxMessageInfo,
                        receiverUser.getUuid()
                );

                System.out.println("==== messageId=" + messageId + " ====");
                System.out.println("wxMessageInfo=" + JSON.toJSONString(wxMessageInfo));
                System.out.println("receiverUser=" + JSON.toJSONString(receiverUser));
                System.out.println("payload=" + JSON.toJSONString(payload));

                if (CollectionUtils.isEmpty(payload.getMedias())) {
                    System.out.println("RESULT: medias empty");
                    continue;
                }

                DownstreamMediaVo media = payload.getMedias().get(0);
                System.out.println("mediaType=" + media.getMediaType());
                System.out.println("mediaUrl=" + media.getMediaUrl());
                System.out.println("rawMediaId=" + media.getRawMediaId());
                System.out.println("fileName=" + media.getFileName());

                if (StringUtils.hasText(media.getMediaUrl())) {
                    System.out.println("RESULT: mediaUrl resolved");
                } else {
                    System.out.println("RESULT: mediaUrl missing");
                }
            }
        } finally {
            context.close();
        }
    }

    private static List<Long> resolveMessageIds(String[] args) {
        List<Long> ids = new ArrayList<Long>();
        if (args == null || args.length == 0) {
            ids.add(DEFAULT_IMAGE_ID);
            ids.add(DEFAULT_VIDEO_ID);
            return ids;
        }

        for (String arg : args) {
            if (!StringUtils.hasText(arg)) {
                continue;
            }
            String[] parts = arg.split(",");
            for (String part : parts) {
                if (StringUtils.hasText(part)) {
                    ids.add(Long.valueOf(part.trim()));
                }
            }
        }

        if (ids.isEmpty()) {
            ids.add(DEFAULT_IMAGE_ID);
            ids.add(DEFAULT_VIDEO_ID);
        }
        return ids;
    }
}
