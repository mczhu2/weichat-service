package com.weichat.api.job;

import com.weichat.api.service.mass.MassTaskPlanExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 群发计划定时任务。
 * 每分钟扫描一次到期计划，并物化为可执行的群发任务。
 */
@Slf4j
@Component
public class MassTaskPlanJob {

    @Autowired
    private MassTaskPlanExecutionService massTaskPlanExecutionService;

    /**
     * 扫描已到 nextTriggerTime 的计划。
     */
    @Scheduled(fixedRate = 60000)
    public void processDuePlans() {
        try {
            massTaskPlanExecutionService.processDuePlans(20);
        } catch (Exception e) {
            log.error("process mass task plans failed", e);
        }
    }
}
