-- 在 wx_user_info 表新增 online_check_disabled 字段
-- 0 表示参与在线状态检查，1 表示停用在线状态检查
ALTER TABLE `wx_user_info`
ADD COLUMN `online_check_disabled` tinyint NOT NULL DEFAULT '0' COMMENT '在线状态检查禁用标记：0-启用，1-停用' AFTER `last_pulled_message_id`;
