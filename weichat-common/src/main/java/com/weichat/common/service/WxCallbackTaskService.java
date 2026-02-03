package com.weichat.common.service;

import com.weichat.common.entity.WxCallbackTask;

import java.util.List;

public interface WxCallbackTaskService {

    int insert(WxCallbackTask task);

    WxCallbackTask selectByPrimaryKey(Long id);

    List<WxCallbackTask> selectPendingTasks(int limit);

    int updateStatus(Long id, Integer status, String errorMessage);

    int incrementRetryCount(Long id);

    boolean tryLockTask(Long id);
}
