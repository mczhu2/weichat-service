package com.weichat.common.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Job 分片信息。
 * 用于向具体的定时任务暴露“当前节点是否有效、属于第几个分片、总分片数是多少”。
 */
@Getter
public class JobShardingInfo {

    /** 当前节点负责的分片编号，从 0 开始。 */
    private final int shardIndex;

    /** 当前活跃节点总数，也就是总分片数。 */
    private final int shardCount;

    /** 当前节点 ID。 */
    private final String nodeId;

    /** 当前节点是否仍然在活跃节点列表中。 */
    private final boolean valid;

    /** 当前任务看到的全部活跃节点列表，按字典序稳定排序。 */
    private final List<String> activeNodeIds;

    public JobShardingInfo(int shardIndex, int shardCount, String nodeId, boolean valid, List<String> activeNodeIds) {
        this.shardIndex = shardIndex;
        this.shardCount = shardCount;
        this.nodeId = nodeId;
        this.valid = valid;
        this.activeNodeIds = activeNodeIds == null ? Collections.emptyList() : Collections.unmodifiableList(activeNodeIds);
    }

    public static JobShardingInfo invalid(String nodeId) {
        return new JobShardingInfo(-1, 0, nodeId, false, Collections.emptyList());
    }
}
