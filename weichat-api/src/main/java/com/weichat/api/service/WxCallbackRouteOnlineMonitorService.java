package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.common.dto.WxCallbackRouteMonitorTarget;
import com.weichat.common.service.WxUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 企微回调路由在线状态巡检服务。
 * <p>按 wx_user_info.user_id 只检查最新绑定的设备 UUID，避免旧设备路由重复告警。</p>
 */
@Slf4j
@Service
public class WxCallbackRouteOnlineMonitorService {

    private static final String RUN_CLIENT_URL = "http://ai-agent.okgcc.cn/wx/api/v1/init/getRunClientByUuid";
    private static final String FEISHU_WEBHOOK_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/a1693cda-b8a2-49d3-b2fc-028019d16fad";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:152.0) Gecko/20100101 Firefox/152.0";
    private static final String REFERER = "http://ai-agent.okgcc.cn/wx/wecom-api-call-service.html";
    private static final String ORIGIN = "http://ai-agent.okgcc.cn";
    private static final int SUCCESS_CODE = 0;
    private static final int LOGIN_TYPE_ONLINE = 2;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 检查已配置回调路由的最新绑定设备在线状态，并将离线账号合并发送飞书告警。
     */
    public void checkAndNotifyOfflineRoutes() {
        List<WxCallbackRouteMonitorTarget> targets = wxUserInfoService.selectCallbackRouteMonitorTargets();
        if (targets.isEmpty()) {
            log.info("callback route online monitor found no targets");
            return;
        }

        List<WxCallbackRouteMonitorTarget> offlineTargets = new ArrayList<>(targets.size());
        for (WxCallbackRouteMonitorTarget target : targets) {
            if (!isOnline(target.getUuid())) {
                offlineTargets.add(target);
            }
        }

        if (offlineTargets.isEmpty()) {
            log.info("callback route online monitor completed, targets={}, offline=0", targets.size());
            return;
        }

        sendFeishuNotification(offlineTargets);
        log.warn("callback route online monitor detected offline routes, targets={}, offline={}", targets.size(), offlineTargets.size());
    }

    /**
     * 调用 getRunClientByUuid 判断账号是否在线。
     *
     * @param uuid 企微运行实例 UUID
     * @return true 表示接口成功且 loginType=2、user_info.isLogin=true
     */
    public boolean isOnline(String uuid) {
        if (!StringUtils.hasText(uuid)) {
            return false;
        }
        JSONObject body = new JSONObject();
        body.put("uuid", uuid);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    RUN_CLIENT_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(body.toJSONString(), buildRunClientHeaders()),
                    String.class
            );
            return parseOnlineState(response.getBody());
        } catch (RestClientResponseException e) {
            log.warn("check run client failed with response, uuid={}, statusCode={}, responseBody={}",
                    uuid, e.getRawStatusCode(), e.getResponseBodyAsString(), e);
            return false;
        } catch (Exception e) {
            log.warn("check run client failed, uuid={}", uuid, e);
            return false;
        }
    }

    private boolean parseOnlineState(String responseBody) {
        if (!StringUtils.hasText(responseBody)) {
            return false;
        }
        JSONObject response = JSONObject.parseObject(responseBody);
        if (response == null || response.getIntValue("code") != SUCCESS_CODE) {
            return false;
        }
        JSONObject data = response.getJSONObject("data");
        if (data == null || data.getIntValue("loginType") != LOGIN_TYPE_ONLINE) {
            return false;
        }
        JSONObject userInfo = data.getJSONObject("user_info");
        return userInfo != null && Boolean.TRUE.equals(userInfo.getBoolean("isLogin"));
    }

    private HttpHeaders buildRunClientHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,zh-TW;q=0.8,zh-HK;q=0.7,en-US;q=0.6,en;q=0.5");
        headers.set(HttpHeaders.REFERER, REFERER);
        headers.set(HttpHeaders.ORIGIN, ORIGIN);
        headers.set("Priority", "u=0");
        return headers;
    }

    private void sendFeishuNotification(List<WxCallbackRouteMonitorTarget> offlineTargets) {
        JSONObject body = new JSONObject();
        body.put("msg_type", "text");
        JSONObject content = new JSONObject();
        content.put("text", buildNotificationText(offlineTargets));
        body.put("content", content);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    FEISHU_WEBHOOK_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(body.toJSONString(), buildFeishuHeaders()),
                    String.class
            );
            log.info("feishu offline route notification sent, count={}, response={}", offlineTargets.size(), response.getBody());
        } catch (RestClientResponseException e) {
            log.error("send feishu offline route notification failed with response, statusCode={}, responseBody={}",
                    e.getRawStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("send feishu offline route notification failed", e);
        }
    }

    private HttpHeaders buildFeishuHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String buildNotificationText(List<WxCallbackRouteMonitorTarget> offlineTargets) {
        StringBuilder builder = new StringBuilder(offlineTargets.size() * 96);
        builder.append("【企微回调路由离线告警】\n")
                .append("以下已配置回调路由的账号不在线，请及时修复：\n");
        for (int i = 0; i < offlineTargets.size(); i++) {
            WxCallbackRouteMonitorTarget target = offlineTargets.get(i);
            builder.append(i + 1)
                    .append(". 用户：")
                    .append(resolveDisplayName(target))
                    .append("\n   UUID：")
                    .append(target.getUuid())
                    .append(" 不在线\n");
        }
        return builder.toString();
    }

    private String resolveDisplayName(WxCallbackRouteMonitorTarget target) {
        if (StringUtils.hasText(target.getNickname())) {
            return target.getNickname();
        }
        if (StringUtils.hasText(target.getRealname())) {
            return target.getRealname();
        }
        if (StringUtils.hasText(target.getAcctid())) {
            return target.getAcctid();
        }
        return String.valueOf(target.getUserId());
    }
}
