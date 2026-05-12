package com.weichat.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Job 心跳管理器。
 * 作用：
 * 1. 将本实例注册到 Redis；
 * 2. 定时续约心跳，维持活跃状态；
 * 3. 清理超时节点，避免下线节点长期参与分片计算。
 */
@Slf4j
@Component
public class JobHeartbeatManager {

    private static final String REDIS_KEY_PREFIX = "weichat:job:sharding:";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /** 节点超时时间，超过这个时间没有心跳就会被踢出活跃节点列表。 */
    @Value("${job.sharding.node-timeout-seconds:180}")
    private long nodeTimeoutSeconds;

    /** 本实例内注册过的 job 节点集合，用于统一续约心跳。 */
    private final Map<String, Set<String>> registeredJobs = new ConcurrentHashMap<>();

    public void registerJob(String jobName, String nodeId) {
        registeredJobs.computeIfAbsent(jobName, key -> ConcurrentHashMap.newKeySet()).add(nodeId);
        updateHeartbeat(jobName, nodeId, System.currentTimeMillis());
        log.info("registered job shard node job={} node={}", jobName, nodeId);
    }

    /** 定时续约所有已注册 job 的心跳。 */
    @Scheduled(fixedRateString = "${job.sharding.heartbeat-interval-ms:30000}")
    public void sendHeartbeat() {
        if (registeredJobs.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Set<String>> entry : registeredJobs.entrySet()) {
            for (String nodeId : entry.getValue()) {
                updateHeartbeat(entry.getKey(), nodeId, now);
            }
        }
    }

    /** 定时清理 Redis 中已超时的节点。 */
    @Scheduled(fixedRateString = "${job.sharding.cleanup-interval-ms:120000}")
    public void cleanupExpiredNodes() {
        if (registeredJobs.isEmpty()) {
            return;
        }
        long staleBefore = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(nodeTimeoutSeconds);
        for (String jobName : registeredJobs.keySet()) {
            cleanupExpiredNodes(jobName, staleBefore);
        }
    }

    public String redisKey(String jobName) {
        return REDIS_KEY_PREFIX + jobName;
    }

    public long staleBeforeMillis() {
        return System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(nodeTimeoutSeconds);
    }

    /**
     * 清理指定 job 的过期节点。
     * 这个方法会在定时清理和分片计算前共用。
     */
    public void cleanupExpiredNodes(String jobName, long staleBeforeMillis) {
        String redisKey = redisKey(jobName);
        Map<Object, Object> nodes = stringRedisTemplate.opsForHash().entries(redisKey);
        if (nodes.isEmpty()) {
            return;
        }
        java.util.List<Object> expiredNodeIds = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : nodes.entrySet()) {
            long lastSeen = parseHeartbeat(entry.getValue());
            if (lastSeen < staleBeforeMillis) {
                expiredNodeIds.add(entry.getKey());
            }
        }
        if (!expiredNodeIds.isEmpty()) {
            stringRedisTemplate.opsForHash().delete(redisKey, expiredNodeIds.toArray());
            log.info("cleaned expired job shard nodes job={} count={}", jobName, expiredNodeIds.size());
        }
    }

    private void updateHeartbeat(String jobName, String nodeId, long timestampMillis) {
        String redisKey = redisKey(jobName);
        stringRedisTemplate.opsForHash().put(redisKey, nodeId, String.valueOf(timestampMillis));
        // 给整张 Hash 设置更长过期时间，防止最后一个节点退出后 key 长期残留。
        stringRedisTemplate.expire(redisKey, nodeTimeoutSeconds * 2, TimeUnit.SECONDS);
    }

    private long parseHeartbeat(Object rawValue) {
        if (rawValue == null) {
            return 0L;
        }
        try {
            return Long.parseLong(String.valueOf(rawValue));
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }
}
