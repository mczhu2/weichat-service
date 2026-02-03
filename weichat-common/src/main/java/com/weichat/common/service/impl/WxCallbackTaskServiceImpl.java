package com.weichat.common.service.impl;

import com.weichat.common.entity.WxCallbackTask;
import com.weichat.common.mapper.WxCallbackTaskMapper;
import com.weichat.common.service.WxCallbackTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxCallbackTaskServiceImpl implements WxCallbackTaskService {

    @Autowired
    private WxCallbackTaskMapper wxCallbackTaskMapper;

    @Override
    public int insert(WxCallbackTask task) {
        return wxCallbackTaskMapper.insert(task);
    }

    @Override
    public WxCallbackTask selectByPrimaryKey(Long id) {
        return wxCallbackTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<WxCallbackTask> selectPendingTasks(int limit) {
        return wxCallbackTaskMapper.selectPendingTasks(limit);
    }

    @Override
    public int updateStatus(Long id, Integer status, String errorMessage) {
        return wxCallbackTaskMapper.updateStatus(id, status, errorMessage);
    }

    @Override
    public int incrementRetryCount(Long id) {
        return wxCallbackTaskMapper.incrementRetryCount(id);
    }

    @Override
    public boolean tryLockTask(Long id) {
        return wxCallbackTaskMapper.updateStatusToProcessing(id, WxCallbackTask.STATUS_PENDING) > 0;
    }
}
