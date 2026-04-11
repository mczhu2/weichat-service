package com.weichat.common.service;

import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.mapper.MassTaskDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MassTaskDetailService {

    @Autowired
    private MassTaskDetailMapper massTaskDetailMapper;

    public List<MassTaskDetail> getUnsentMassTaskDetails(int limit) {
        return massTaskDetailMapper.selectUnsentDetails(limit);
    }

    public void updateSendSuccessStatus(Long detailId) {
        MassTaskDetail detail = new MassTaskDetail();
        detail.setId(detailId);
        detail.setIsSent(1);
        detail.setSendStatus(1);
        detail.setSendTime(LocalDateTime.now());
        detail.setUpdateTime(LocalDateTime.now());
        massTaskDetailMapper.updateByPrimaryKey(detail);
    }

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

    public List<MassTaskDetail> getDetailsByTaskId(Long taskId) {
        return massTaskDetailMapper.selectByTaskId(taskId);
    }

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

    public int deleteByTaskId(Long taskId) {
        return massTaskDetailMapper.deleteByTaskId(taskId);
    }

    public boolean removeById(Long id) {
        return massTaskDetailMapper.deleteByPrimaryKey(id) > 0;
    }
}
