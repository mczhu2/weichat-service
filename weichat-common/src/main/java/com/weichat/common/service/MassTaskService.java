package com.weichat.common.service;

import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.mapper.MassTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MassTaskService {

    @Autowired
    private MassTaskMapper massTaskMapper;

    @Autowired
    private MassTaskDetailService massTaskDetailService;

    @Transactional
    public Long createMassTask(MassTask massTask, List<MassTaskDetail> details) {
        massTask.setCreateTime(LocalDateTime.now());
        massTask.setSendStatus(0);
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

    public void updateTaskStatus(Long taskId, Integer status) {
        MassTask massTask = new MassTask();
        massTask.setId(taskId);
        massTask.setSendStatus(status);
        massTaskMapper.updateByPrimaryKey(massTask);
    }

    public List<MassTask> getMassTaskList(int offset, int limit) {
        return massTaskMapper.selectAllWithPaging(offset, limit);
    }

    public MassTask getMassTaskById(Long taskId) {
        return massTaskMapper.selectByPrimaryKey(taskId);
    }

    public void updateTaskStatistics(Long taskId, Integer sentCount, Integer successCount) {
        MassTask massTask = new MassTask();
        massTask.setId(taskId);
        massTask.setSentCount(sentCount);
        massTask.setSuccessCount(successCount);

        MassTask originalTask = massTaskMapper.selectByPrimaryKey(taskId);
        if (originalTask != null && sentCount >= originalTask.getTotalCount()) {
            massTask.setSendStatus(2);
        }

        massTaskMapper.updateByPrimaryKey(massTask);
    }

    public void cancelMassTask(Long taskId) {
        MassTask massTask = new MassTask();
        massTask.setId(taskId);
        massTask.setSendStatus(3);
        massTaskMapper.updateByPrimaryKey(massTask);
    }

    public void updateById(MassTask massTask) {
        massTaskMapper.updateByPrimaryKey(massTask);
    }

    @Transactional
    public boolean removeById(Long id) {
        massTaskDetailService.deleteByTaskId(id);
        return massTaskMapper.deleteByPrimaryKey(id) > 0;
    }
}
