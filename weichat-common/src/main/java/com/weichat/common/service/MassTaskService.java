package com.weichat.common.service;

import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.enums.MassTaskSendStatusEnum;
import com.weichat.common.mapper.MassTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 群发任务服务，负责任务主表的创建、状态变更和统计维护。
 */
@Service
public class MassTaskService {

    @Autowired
    private MassTaskMapper massTaskMapper;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    /**
     * 创建群发任务，并同步写入对应的任务明细。
     */
    @Transactional
    public Long createMassTask(MassTask massTask, List<MassTaskDetail> details) {
        massTask.setCreateTime(LocalDateTime.now());
        massTask.setSendStatus(MassTaskSendStatusEnum.PENDING.getCode());
        massTask.setTotalCount(details.size());
        massTask.setSentCount(0);
        massTask.setSuccessCount(0);

        massTaskMapper.insert(massTask);

        for (MassTaskDetail detail : details) {
            detail.setTaskId(massTask.getId());
        }

        massTaskDetailService.batchInsertDetails(details);
        return massTask.getId();
    }

    /**
     * 更新任务执行状态。
     */
    public void updateTaskStatus(Long taskId, Integer status) {
        MassTask massTask = new MassTask();
        massTask.setId(taskId);
        massTask.setSendStatus(status);
        massTaskMapper.updateByPrimaryKey(massTask);
    }

    /**
     * 分页查询任务列表。
     */
    public List<MassTask> getMassTaskList(int offset, int limit) {
        return massTaskMapper.selectAllWithPaging(offset, limit);
    }

    public int countMassTasks() {
        return massTaskMapper.countAll();
    }

    public int countByPlanIdAndScheduleSlot(Long planId, String scheduleSlot) {
        return massTaskMapper.countByPlanIdAndScheduleSlot(planId, scheduleSlot);
    }

    /**
     * 根据任务 ID 查询任务详情。
     */
    public MassTask getMassTaskById(Long taskId) {
        return massTaskMapper.selectByPrimaryKey(taskId);
    }

    /**
     * 更新任务的已发送数和成功数。
     */
    public void updateTaskStatistics(Long taskId, Integer sentCount, Integer successCount) {
        MassTask massTask = new MassTask();
        massTask.setId(taskId);
        massTask.setSentCount(sentCount);
        massTask.setSuccessCount(successCount);

        MassTask originalTask = massTaskMapper.selectByPrimaryKey(taskId);
        if (originalTask != null && sentCount >= originalTask.getTotalCount()) {
            massTask.setSendStatus(MassTaskSendStatusEnum.COMPLETED.getCode());
        }

        massTaskMapper.updateByPrimaryKey(massTask);
    }

    /**
     * 将任务标记为已取消。
     */
    public void cancelMassTask(Long taskId) {
        MassTask massTask = new MassTask();
        massTask.setId(taskId);
        massTask.setSendStatus(MassTaskSendStatusEnum.CANCELLED.getCode());
        massTaskMapper.updateByPrimaryKey(massTask);
    }

    /**
     * 更新任务主表记录。
     */
    public void updateById(MassTask massTask) {
        massTaskMapper.updateByPrimaryKey(massTask);
    }

    /**
     * 删除任务，并级联删除任务明细。
     */
    @Transactional
    public boolean removeById(Long id) {
        massTaskDetailService.deleteByTaskId(id);
        return massTaskMapper.deleteByPrimaryKey(id) > 0;
    }
}
