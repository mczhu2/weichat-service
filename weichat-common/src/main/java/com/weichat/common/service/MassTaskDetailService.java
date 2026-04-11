package com.weichat.common.service;

import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.mapper.MassTaskDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 群发任务明细服务，负责单个接收对象的发送状态维护。
 */
@Service
public class MassTaskDetailService {

    @Autowired
    private MassTaskDetailMapper massTaskDetailMapper;

    /**
     * 查询待发送的任务明细。
     */
    public List<MassTaskDetail> getUnsentMassTaskDetails(int limit) {
        return massTaskDetailMapper.selectUnsentDetails(limit);
    }

    /**
     * 将明细标记为发送成功。
     */
    public void updateSendSuccessStatus(Long detailId) {
        MassTaskDetail detail = new MassTaskDetail();
        detail.setId(detailId);
        detail.setIsSent(1);
        detail.setSendStatus(1);
        detail.setSendTime(LocalDateTime.now());
        detail.setUpdateTime(LocalDateTime.now());
        massTaskDetailMapper.updateByPrimaryKey(detail);
    }

    /**
     * 将明细标记为发送失败，并记录失败原因。
     */
    public void updateSendFailureStatus(Long detailId, String errorMsg) {
        MassTaskDetail detail = new MassTaskDetail();
        detail.setId(detailId);
        detail.setIsSent(1);
        detail.setSendStatus(0);
        detail.setSendResult(errorMsg);
        detail.setSendTime(LocalDateTime.now());
        detail.setUpdateTime(LocalDateTime.now());
        massTaskDetailMapper.updateByPrimaryKey(detail);
    }

    /**
     * 根据任务 ID 查询全部明细。
     */
    public List<MassTaskDetail> getDetailsByTaskId(Long taskId) {
        return massTaskDetailMapper.selectByTaskId(taskId);
    }

    /**
     * 批量写入任务明细。
     */
    public int batchInsertDetails(List<MassTaskDetail> details) {
        int count = 0;
        for (MassTaskDetail detail : details) {
            detail.setCreateTime(LocalDateTime.now());
            detail.setIsSent(0);
            detail.setSendStatus(0);
            massTaskDetailMapper.insert(detail);
            count++;
        }
        return count;
    }

    /**
     * 根据任务 ID 删除全部明细。
     */
    public int deleteByTaskId(Long taskId) {
        return massTaskDetailMapper.deleteByTaskId(taskId);
    }

    /**
     * 删除单条任务明细。
     */
    public boolean removeById(Long id) {
        return massTaskDetailMapper.deleteByPrimaryKey(id) > 0;
    }
}
