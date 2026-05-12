package com.weichat.api.job;

import com.weichat.api.entity.CallbackRequest;
import com.weichat.api.strategy.CallbackStrategyFactory;
import com.weichat.common.dto.JobShardingInfo;
import com.weichat.common.entity.WxCallbackTask;
import com.weichat.common.service.JobShardingService;
import com.weichat.common.service.WxCallbackTaskService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CallbackTaskProcessJob {

    private static final Logger logger = LoggerFactory.getLogger(CallbackTaskProcessJob.class);
    private static final int BATCH_SIZE = 100;
    private static final String JOB_NAME = "callback-task-process-job";
    private static final String MDC_KEY = "traceId";

    @Autowired
    private WxCallbackTaskService callbackTaskService;

    @Autowired
    private CallbackStrategyFactory callbackStrategyFactory;

    @Autowired
    private JobShardingService jobShardingService;

    /** 当前进程的节点 ID，用于在 Redis 中登记和计算分片。 */
    private String nodeId;

    @PostConstruct
    public void init() {
        nodeId = UUID.randomUUID().toString();
        jobShardingService.registerNode(JOB_NAME, nodeId);
    }

    @Scheduled(fixedDelay = 5000)
    public void processCallbackTasks() {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put(MDC_KEY, traceId);
        try {
            JobShardingInfo shardingInfo = jobShardingService.getShardingInfo(JOB_NAME, nodeId);
            if (!shardingInfo.isValid()) {
                logger.warn("callback task shard invalid node={}", nodeId);
                return;
            }
            List<WxCallbackTask> tasks = callbackTaskService.selectPendingTasks(
                    BATCH_SIZE,
                    shardingInfo.getShardIndex(),
                    shardingInfo.getShardCount()
            );
            if (tasks.isEmpty()) {
                return;
            }

            logger.info("开始处理回调任务，数量: {}", tasks.size());

            for (WxCallbackTask task : tasks) {
                processTask(task);
            }
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    private void processTask(WxCallbackTask task) {
        if (!callbackTaskService.tryLockTask(task.getId())) {
            return;
        }

        try {
            CallbackRequest request = buildCallbackRequest(task);
            
            if (callbackStrategyFactory.getStrategy(task.getType()) != null) {
                callbackStrategyFactory.getStrategy(task.getType()).handle(request);
                callbackTaskService.updateStatus(task.getId(), WxCallbackTask.STATUS_SUCCESS, null);
                logger.info("回调任务处理成功, id: {}, type: {}", task.getId(), task.getType());
            } else {
                callbackTaskService.updateStatus(task.getId(), WxCallbackTask.STATUS_FAILED, "未知的回调类型");
                logger.warn("未知的回调类型: {}", task.getType());
            }
        } catch (Exception e) {
            handleTaskError(task, e);
        }
    }

    private CallbackRequest buildCallbackRequest(WxCallbackTask task) {
        CallbackRequest request = new CallbackRequest();
        request.setUuid(task.getUuid());
        request.setJson(task.getJsonContent());
        request.setType(task.getType());
        return request;
    }

    private void handleTaskError(WxCallbackTask task, Exception e) {
        logger.error("处理回调任务失败, id: {}, type: {}, error: {}", task.getId(), task.getType(), e.getMessage(), e);
        callbackTaskService.incrementRetryCount(task.getId());
        
        WxCallbackTask updated = callbackTaskService.selectByPrimaryKey(task.getId());
        if (updated.getRetryCount() >= updated.getMaxRetryCount()) {
            callbackTaskService.updateStatus(task.getId(), WxCallbackTask.STATUS_FAILED, e.getMessage());
        } else {
            callbackTaskService.updateStatus(task.getId(), WxCallbackTask.STATUS_PENDING, e.getMessage());
        }
    }
}
