package com.weichat.api.job;

import com.weichat.api.service.MassMessageService;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.service.MassTaskDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MassMessageJob {

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Autowired
    private MassMessageService massMessageService;

    @Scheduled(fixedRate = 60000)
    public void processUnsentMassMessages() {
        log.info("开始执行群发消息定时任务");

        try {
            List<MassTaskDetail> unsentDetails = massTaskDetailService.getUnsentMassTaskDetails(10);
            if (unsentDetails.isEmpty()) {
                log.info("当前没有需要发送的群发消息");
                return;
            }

            log.info("找到 {} 条待发送的群发消息", unsentDetails.size());
            for (MassTaskDetail detail : unsentDetails) {
                try {
                    boolean success = massMessageService.sendMassMessageToReceiver(detail);
                    if (success) {
                        log.info("群发消息发送成功，明细ID: {}", detail.getId());
                    } else {
                        log.warn("群发消息发送失败，明细ID: {}", detail.getId());
                    }
                } catch (Exception e) {
                    log.error("处理单条群发消息时发生异常，明细ID: {}", detail.getId(), e);
                }
            }

            log.info("群发消息定时任务执行完成");
        } catch (Exception e) {
            log.error("执行群发消息定时任务时发生异常", e);
        }
    }
}
