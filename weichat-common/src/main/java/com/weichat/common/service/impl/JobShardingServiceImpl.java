package com.weichat.common.service.impl;

import com.weichat.common.dto.JobShardingInfo;
import com.weichat.common.service.JobHeartbeatManager;
import com.weichat.common.service.JobShardingService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Redis 分片服务实现。
 * 规则很简单：
 * 1. 每个实例将 nodeId 注册到 Redis；
 * 2. 所有活跃节点按字典序排序；
 * 3. 当前节点在排序后列表中的下标，就是它负责的 shardIndex。
 */
@Service
public class JobShardingServiceImpl implements JobShardingService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JobHeartbeatManager jobHeartbeatManager;

    @Override
    public void registerNode(String jobName, String nodeId) {
        jobHeartbeatManager.registerJob(jobName, nodeId);
    }

    @Override
    public JobShardingInfo getShardingInfo(String jobName, String nodeId) {
        long staleBefore = jobHeartbeatManager.staleBeforeMillis();
        jobHeartbeatManager.cleanupExpiredNodes(jobName, staleBefore);

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(jobHeartbeatManager.redisKey(jobName));
        if (entries.isEmpty()) {
            return JobShardingInfo.invalid(nodeId);
        }

        List<String> activeNodeIds = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            long lastSeen;
            try {
                lastSeen = Long.parseLong(String.valueOf(entry.getValue()));
            } catch (NumberFormatException ignored) {
                continue;
            }
            if (lastSeen >= staleBefore) {
                activeNodeIds.add(String.valueOf(entry.getKey()));
            }
        }
        if (activeNodeIds.isEmpty()) {
            return JobShardingInfo.invalid(nodeId);
        }

        Collections.sort(activeNodeIds);
        int shardIndex = activeNodeIds.indexOf(nodeId);
        if (shardIndex < 0) {
            return JobShardingInfo.invalid(nodeId);
        }
        return new JobShardingInfo(shardIndex, activeNodeIds.size(), nodeId, true, activeNodeIds);
    }
}
