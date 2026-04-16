-- 在 wx_user_info 表新增 last_pulled_message_id 字段
-- 用于记录该用户已拉取的最大消息ID，保证增量拉取时从上次位置继续
ALTER TABLE `wx_user_info` 
ADD COLUMN `last_pulled_message_id` bigint DEFAULT NULL COMMENT '上次拉取的最大消息ID' AFTER `is_del`;
