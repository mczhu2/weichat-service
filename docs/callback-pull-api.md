# 回调消息回拉 API

## 接口说明

根据设备 uuid 回拉消息记录，支持全量拉取和增量拉取两种模式。

## 接口地址

```
POST /api/v1/callback/pull
```

## 请求参数

### Headers
```
Content-Type: application/json
```

### Body

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| uuid | String | 是 | 设备唯一标识 | "uuid-xxxx-xxxx" |
| dataRangeType | Integer | 是 | 数据范围类型：0-从新开始拉取(全量)，1-拉取增量数据 | 1 |
| pageNum | Integer | 否 | 每页数量，默认 10 | 10 |

### 请求示例

#### 增量拉取（推荐）
```json
{
  "uuid": "uuid-xxxx-xxxx",
  "dataRangeType": 1,
  "pageNum": 10
}
```

#### 全量拉取（从历史最小ID开始）
```json
{
  "uuid": "uuid-xxxx-xxxx",
  "dataRangeType": 0,
  "pageNum": 10
}
```

## 响应结果

### 成功响应

```json
{
  "code": 0,
  "msg": "ok",
  "data": {
    "jsonContentList": [
      "{...}", // 回调消息内容JSON
      "{...}"
    ],
    "hasMore": true,
    "currentMaxId": 30217
  }
}
```

### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| jsonContentList | List\<String\> | 回调任务 JSON 内容列表 |
| hasMore | Boolean | 是否还有更多数据，true 表示可以继续拉取 |
| currentMaxId | Long | 本次拉取的最大消息 ID，用于下次增量拉取 |

## 使用说明

1. **首次拉取或全量同步**：使用 `dataRangeType=0`，会从该 uuid 对应的历史最小消息 ID 开始拉取
2. **增量拉取**：使用 `dataRangeType=1`，系统会自动从上次拉取的位置继续拉取最新数据
3. **分页控制**：通过 `pageNum` 控制每次拉取的消息数量
4. **游标管理**：系统会自动在 `wx_user_info` 表中记录拉取到的最大消息 ID，保证增量拉取的准确性
5. **连续拉取**：当 `hasMore=true` 时，可以继续使用相同参数发起请求获取更多数据

## 数据库变更

需要执行以下 SQL 脚本添加必要字段：

```sql
-- 在 wx_user_info 表新增 last_pulled_message_id 字段
ALTER TABLE `wx_user_info` 
ADD COLUMN `last_pulled_message_id` bigint DEFAULT NULL COMMENT '上次拉取的最大消息ID' AFTER `is_del`;
```

## 注意事项

- 消息按照 ID 从小到大顺序拉取
- 增量拉取模式下，只会拉取大于上次拉取位置的新消息
- 如果用户信息不存在，会返回错误提示
