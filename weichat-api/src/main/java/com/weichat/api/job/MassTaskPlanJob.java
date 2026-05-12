package com.weichat.api.job;

import com.weichat.api.service.mass.MassTaskPlanExecutionService;
import com.weichat.common.dto.JobShardingInfo;
import com.weichat.common.service.JobShardingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * 群发计划定时任务。
 * 每分钟扫描一次到期计划，并物化为可执行的群发任务。
 */
@Slf4j
@Component
public class MassTaskPlanJob {

    @Autowired
    private MassTaskPlanExecutionService massTaskPlanExecutionService;

    @Autowired
    private JobShardingService jobShardingService;

    private static final String JOB_NAME = "mass-task-plan-job";

    /** 当前进程的节点 ID，用于在 Redis 中登记和计算分片。 */
    private String nodeId;

    @PostConstruct
    public void init() {
        nodeId = UUID.randomUUID().toString();
        jobShardingService.registerNode(JOB_NAME, nodeId);
    }

    /**
     * 扫描已到 nextTriggerTime 的计划。
     */
    @Scheduled(fixedRate = 60000)
    public void processDuePlans() {
        try {
            JobShardingInfo shardingInfo = jobShardingService.getShardingInfo(JOB_NAME, nodeId);
            if (!shardingInfo.isValid()) {
                log.warn("mass task plan shard invalid node={}", nodeId);
                return;
            }
            // 直接按计划 ID 做分片查询，避免多实例重复扫描同一批计划。
            massTaskPlanExecutionService.processDuePlans(
                    20,
                    shardingInfo.getShardIndex(),
                    shardingInfo.getShardCount()
            );
        } catch (Exception e) {
            log.error("process mass task plans failed", e);
        }
    }
}
