package com.weichat.api.job;

import com.weichat.api.service.WxCallbackRouteOnlineMonitorService;
import com.weichat.common.dto.JobShardingInfo;
import com.weichat.common.service.JobShardingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * 企微回调路由在线状态巡检任务。
 * <p>每 5 分钟检查一次配置了回调路由的最新绑定设备是否在线。</p>
 */
@Slf4j
@Component
public class WxCallbackRouteOnlineMonitorJob {

    private static final String JOB_NAME = "wx-callback-route-online-monitor-job";

    @Autowired
    private WxCallbackRouteOnlineMonitorService monitorService;

    @Autowired
    private JobShardingService jobShardingService;

    /** 当前进程的节点 ID，用于在 Redis 中登记和计算分片。 */
    private String nodeId;

    @PostConstruct
    public void init() {
        nodeId = UUID.randomUUID().toString();
        jobShardingService.registerNode(JOB_NAME, nodeId);
    }

    /**
     * 每 5 分钟检查一次账号在线状态。
     */
    @Scheduled(fixedRate = 300000)
    public void checkCallbackRouteOnlineState() {
        try {
            JobShardingInfo shardingInfo = jobShardingService.getShardingInfo(JOB_NAME, nodeId);
            if (!shardingInfo.isValid()) {
                log.warn("callback route online monitor shard invalid node={}", nodeId);
                return;
            }
            if (shardingInfo.getShardIndex() != 0) {
                return;
            }
            monitorService.checkAndNotifyOfflineRoutes();
        } catch (Exception e) {
            log.error("callback route online monitor job failed", e);
        }
    }
}
