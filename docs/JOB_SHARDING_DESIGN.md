# Java 服务分片 Job 技术设计

本文档描述 `weichat-service` 当前定时任务的分片设计，供后续新增 `@Scheduled` 任务时直接复用。

## 1. 目标

`weichat-service` 当前包含多个定时任务，例如：

- `MassTaskPlanJob`
- `MassMessageJob`
- `CallbackTaskProcessJob`

单实例运行时，这些任务可以正常工作；但在多实例部署时，原始实现会面临两个问题：

1. 多个实例会同时扫描同一批数据，造成重复查询和潜在重复处理。
2. 单实例独自承担全部扫描压力，容易形成性能瓶颈。

本次设计目标是：

- 基于 Redis 构建统一的 job 节点注册、心跳和分片计算能力。
- 让多个实例可以天然水平扩容，每个实例只负责自己的一部分任务数据。
- 保持现有业务处理逻辑尽量不动，把分片能力集中在“job 基座 + mapper 查询”层。

## 2. 设计边界

适用场景：

- 定时轮询型任务
- 能按稳定主键做模分片的数据处理任务
- 允许多实例并行处理不同分片

不直接解决：

- 严格的全局 claim / ack / recovery 流程
- 分布式事务
- 复杂优先级调度

当前设计的重点是“让不同实例不再重复扫描同一份工作集”。

## 3. 总体架构

### 3.1 Redis 分片基座

新增类：

- [weichat-common/src/main/java/com/weichat/common/dto/JobShardingInfo.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/java/com/weichat/common/dto/JobShardingInfo.java)
- [weichat-common/src/main/java/com/weichat/common/service/JobShardingService.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/java/com/weichat/common/service/JobShardingService.java)
- [weichat-common/src/main/java/com/weichat/common/service/JobHeartbeatManager.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/java/com/weichat/common/service/JobHeartbeatManager.java)
- [weichat-common/src/main/java/com/weichat/common/service/impl/JobShardingServiceImpl.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/java/com/weichat/common/service/impl/JobShardingServiceImpl.java)

依赖：

- [weichat-common/pom.xml](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/pom.xml)

新增 `spring-boot-starter-data-redis`，用于：

1. 记录每个 job 的活跃节点
2. 周期续约心跳
3. 清理超时节点
4. 计算当前节点的分片编号

### 3.2 Redis 数据模型

每个 job 对应一个 Redis Hash：

- key：`weichat:job:sharding:{jobName}`

field / value：

- field：`nodeId`
- value：最近心跳时间戳（毫秒）

含义：

- 一个 job 可以有多个活跃节点
- 每个节点在该 Hash 中有一条心跳记录

### 3.3 分片计算规则

步骤：

1. 读取指定 job 的全部节点心跳
2. 剔除超时节点
3. 将活跃节点按 `nodeId` 字典序排序
4. 当前节点在排序后列表中的下标，就是 `shardIndex`
5. 活跃节点总数，就是 `shardCount`

例如：

- 节点列表：`["node-a", "node-b", "node-c"]`
- `node-a -> 0/3`
- `node-b -> 1/3`
- `node-c -> 2/3`

这样每个实例都能稳定得到同样的分片视图。

## 4. 当前已接入任务

### 4.1 群发计划物化任务

文件：

- [weichat-api/src/main/java/com/weichat/api/job/MassTaskPlanJob.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-api/src/main/java/com/weichat/api/job/MassTaskPlanJob.java)

任务名：

- `mass-task-plan-job`

分片键：

- `mass_task_plan.id`

接入方式：

1. job 启动时生成 `nodeId` 并注册
2. 每次执行前计算当前分片信息
3. 查询阶段就带上：
   - `mod(id, shardCount) = shardItem`

下推位置：

- [MassTaskPlanMapper.xml](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/resources/mapper/MassTaskPlanMapper.xml)

这样可以保证：

- 不同实例读取不同分片的到期计划
- 避免多个实例都扫同一批 `mass_task_plan`

### 4.2 群发明细发送任务

文件：

- [weichat-api/src/main/java/com/weichat/api/job/MassMessageJob.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-api/src/main/java/com/weichat/api/job/MassMessageJob.java)

任务名：

- `mass-message-job`

分片键：

- `mass_task_detail.id`

接入方式：

1. job 启动时注册节点
2. 每次执行前获取 `shardIndex / shardCount`
3. SQL 里直接按 `detail.id` 做模分片

下推位置：

- [MassTaskDetailMapper.xml](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/resources/mapper/MassTaskDetailMapper.xml)

这一步是本次改造里最关键的一项，因为它直接决定多实例下是否还会重复扫描相同待发明细。

### 4.3 回调任务消费

文件：

- [weichat-api/src/main/java/com/weichat/api/job/CallbackTaskProcessJob.java](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-api/src/main/java/com/weichat/api/job/CallbackTaskProcessJob.java)

任务名：

- `callback-task-process-job`

分片键：

- `wx_callback_task.id`

接入方式：

1. 先按 `id` 做分片查询
2. 再保留原有 `tryLockTask(id)` 逻辑

下推位置：

- [WxCallbackTaskMapper.xml](/Users/mczhu/Documents/trae_projects/weichat-service/weichat-common/src/main/resources/mapper/WxCallbackTaskMapper.xml)

含义：

- 分片查询负责降低多实例重复扫描
- `tryLockTask` 继续负责同一条回调任务的最终并发保护

这是“分片 + 逐条乐观锁”组合，而不是二选一。

## 5. Service / Mapper 扩展点

为了让 job 层尽量简洁，本次扩展了以下 service / mapper：

### 5.1 `MassTaskPlanService`

新增：

- `getDuePlans(limit, shardItem, shardCount)`

### 5.2 `MassTaskDetailService`

新增：

- `getSchedulableMassTaskDetails(limit, shardItem, shardCount)`

### 5.3 `WxCallbackTaskService`

新增：

- `selectPendingTasks(limit, shardItem, shardCount)`

对应 mapper 也都增加了 `...Sharded` 查询方法。

原则是：

- job 负责获取分片信息
- service 负责暴露分片查询接口
- mapper 负责真正把分片条件下推到 SQL

## 6. 配置项

当前基座支持以下配置：

- `job.sharding.node-timeout-seconds`
- `job.sharding.heartbeat-interval-ms`
- `job.sharding.cleanup-interval-ms`

默认值：

- 节点超时：`180s`
- 心跳间隔：`30000ms`
- 清理间隔：`120000ms`

建议：

1. `node-timeout-seconds` 明显大于心跳间隔
2. 避免设置过短，防止实例轻微抖动就被踢出活跃节点
3. Redis 需要作为稳定共享资源提供给所有 job 实例

## 7. 后续新增 job 接入步骤

如果后续要新增一个支持分片的 `@Scheduled` 任务，推荐按下面步骤接入：

1. 给 job 定义唯一的 `JOB_NAME`
2. 在 job 类中新增：
   - `JobShardingService`
   - `nodeId`
   - `@PostConstruct init()`
3. 在 `init()` 中：
   - 生成 `UUID`
   - `registerNode(JOB_NAME, nodeId)`
4. 每次执行前调用：
   - `getShardingInfo(JOB_NAME, nodeId)`
5. 如果 `!isValid()`，直接跳过
6. 选择稳定分片键，并把分片条件下推到 SQL

推荐分片键选择顺序：

1. 业务主键 `id`
2. 不为空且稳定的唯一业务键
3. 不要使用会变化的文本描述字段

不建议：

- 先 `LIMIT` 再在内存里筛分片
- 注释写一套、实现用另一套分片键
- 依赖可能为空的字段做唯一分片依据

## 8. 设计取舍

### 8.1 为什么选 Redis，不选数据库

因为 `weichat-service` 明确有 Redis 资源可以提供，而且 Java 服务本身更适合把分片协调放到共享缓存层：

- 节点注册和心跳更新更轻
- 多实例共享状态天然合适
- 对数据库主链路压力更小

### 8.2 为什么先做“分片查询”，不做更复杂的 claim 流程

本轮改造的目标是先解决：

- 多实例重复扫描
- 单实例独跑

如果一上来就做：

- 批量 claim
- 处理中状态回收
- 分布式恢复

复杂度会明显上升，也会牵动更多业务状态。

因此当前方案先采用：

- 分片查询
- 原有幂等/锁逻辑尽量保留

其中 `CallbackTaskProcessJob` 已经保留了 `tryLockTask`，是当前最稳妥的形式。

## 9. 验证方式

本次改造已经做过：

1. Java 编译验证
   - `mvn -pl weichat-common,weichat-api -am -DskipTests compile`
2. mapper / service / job 接口联动检查

后续建议补充：

1. 多实例本地联调
   - 同时启动 2 个 `weichat-api`
   - 观察不同实例拿到的 `shardIndex / shardCount`
2. Redis 下线/重连演练
3. job 执行日志中加入：
   - `jobName`
   - `nodeId`
   - `shardIndex`
   - `shardCount`

## 10. 已知限制

1. 当前 `MassMessageJob` 仍未引入批量 claim，主要依赖“查询分片 + 发送后更新状态”。
2. 当前 `MassTaskPlanJob` 也还没有引入更强的全局物化幂等约束，后续仍建议补唯一约束或 claim 机制。
3. Redis 分片解决的是“多实例重复扫描”和“单实例单点”，不等于已经做到最终态的高可靠任务编排。

## 11. 结论

当前 `weichat-service` 的分片 job 方案是：

- 用 Redis 做节点注册和分片协调
- 用稳定排序计算 `shardIndex / shardCount`
- 在 SQL 查询阶段直接下推分片条件
- 尽量保留原有业务处理逻辑

这套设计已经足够作为后续其他定时任务的统一模板。  
如果后续任务复杂度继续上升，可以在此基础上进一步演进为：

- 分片查询 + claim
- 处理中状态超时回收
- 失败重试队列
- 独立 job 监控与告警
