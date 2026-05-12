package com.weichat.common.service;

import com.weichat.common.dto.JobShardingInfo;

/**
 * Job 分片服务接口。
 * 负责节点注册和分片信息计算，供具体的定时任务直接使用。
 */
public interface JobShardingService {

    void registerNode(String jobName, String nodeId);

    JobShardingInfo getShardingInfo(String jobName, String nodeId);
}
