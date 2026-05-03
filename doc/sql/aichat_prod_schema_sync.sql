-- Idempotent schema sync script for weichat-api / aichat.
-- Source of truth:
--   test db from weichat-api/src/main/resources/application.properties
-- Target:
--   prod db from weichat-api/src/main/resources/application-prod.properties
--
-- This script is designed to be safe to run multiple times.
-- It creates missing tables, adds missing columns, normalizes column definitions,
-- adds missing indexes, and adds the mass_task_detail -> mass_task foreign key.

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `mass_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `task_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'task name',
  `plan_id` bigint DEFAULT NULL COMMENT 'mass task plan id',
  `schedule_slot` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'plan execution slot key, such as 20260419090000',
  `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT 'legacy plain text content, mainly used for text message',
  `image_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy image media id, kept for compatibility',
  `file_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy file media id, kept for compatibility',
  `audio_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy voice media id, kept for compatibility',
  `video_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy video media id, kept for compatibility',
  `payload_version` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT 'v1' COMMENT 'rich payload schema version',
  `payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'normalized rich payload json, aligned with MassTaskCreateRequest.payload',
  `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  `template_variables_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'template variable snapshot json at task creation time',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  `send_time` datetime DEFAULT NULL COMMENT 'planned send time',
  `send_status` tinyint NOT NULL DEFAULT '0' COMMENT '0=pending, 1=sending, 2=finished, 3=cancelled, 4=failed',
  `fail_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'task level failure reason',
  `creator` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'creator',
  `total_count` int DEFAULT '0' COMMENT 'total receivers',
  `sent_count` int DEFAULT '0' COMMENT 'sent receivers',
  `success_count` int DEFAULT '0' COMMENT 'successful receivers',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'remark',
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`),
  KEY `idx_task_type` (`task_type`),
  KEY `idx_msg_type` (`msg_type`),
  KEY `idx_send_status` (`send_status`),
  KEY `idx_send_time` (`send_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_schedule_slot` (`schedule_slot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task';

CREATE TABLE IF NOT EXISTS `mass_task_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `task_id` bigint NOT NULL COMMENT 'mass task id',
  `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  `receiver_id` bigint NOT NULL COMMENT 'receiver id',
  `receiver_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'receiver name',
  `message_snapshot_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'rendered payload snapshot for this receiver',
  `is_sent` tinyint NOT NULL DEFAULT '0' COMMENT '0=not sent, 1=sent',
  `planned_send_time` datetime DEFAULT NULL COMMENT 'planned send time within daily window',
  `send_time` datetime DEFAULT NULL COMMENT 'actual send time',
  `send_status` tinyint DEFAULT '0' COMMENT '0=failed, 1=success',
  `send_result` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'send result summary',
  `downstream_response` longtext COLLATE utf8mb4_unicode_ci COMMENT 'raw downstream response body if needed for troubleshooting',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_receiver_id` (`receiver_id`),
  KEY `idx_is_sent` (`is_sent`),
  KEY `idx_send_status` (`send_status`),
  KEY `idx_planned_send_time` (`planned_send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task detail';

CREATE TABLE IF NOT EXISTS `mass_task_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `plan_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'plan name',
  `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  `receiver_ids_json` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'receiver ids json array',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  `payload_version` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  `payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'normalized rich payload json',
  `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  `creator` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'creator',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'remark',
  `schedule_type` tinyint NOT NULL COMMENT '1=once, 2=daily, 3=weekly, 4=monthly',
  `schedule_rule_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'weekly/monthly rule json',
  `effective_start_at` datetime NOT NULL COMMENT 'effective start time',
  `effective_end_at` datetime DEFAULT NULL COMMENT 'effective end time',
  `window_start_time` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'daily send window start time, HH:mm:ss',
  `window_end_time` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'daily send window end time, HH:mm:ss',
  `rate_per_minute` int NOT NULL DEFAULT '10' COMMENT 'max receiver count per minute',
  `timezone` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Asia/Shanghai' COMMENT 'plan timezone',
  `next_trigger_time` datetime DEFAULT NULL COMMENT 'next plan trigger time',
  `last_trigger_time` datetime DEFAULT NULL COMMENT 'last materialized trigger time',
  `plan_status` tinyint NOT NULL DEFAULT '1' COMMENT '1=enabled, 2=paused, 3=cancelled, 4=finished',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_plan_status` (`plan_status`),
  KEY `idx_next_trigger_time` (`next_trigger_time`),
  KEY `idx_effective_start_at` (`effective_start_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task plan';

CREATE TABLE IF NOT EXISTS `message_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `template_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'template name',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  `template_content` text COLLATE utf8mb4_unicode_ci COMMENT 'legacy text template content, mainly used for text template',
  `payload_version` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  `payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'rich template payload json',
  `variables` longtext COLLATE utf8mb4_unicode_ci COMMENT 'template variable definition json',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'remark',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  `creator` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'creator',
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`),
  KEY `idx_msg_type` (`msg_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='message template';

ALTER TABLE `mass_task`
  MODIFY COLUMN `task_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'task name',
  MODIFY COLUMN `plan_id` bigint DEFAULT NULL COMMENT 'mass task plan id',
  MODIFY COLUMN `schedule_slot` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'plan execution slot key, such as 20260419090000',
  MODIFY COLUMN `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  MODIFY COLUMN `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  MODIFY COLUMN `content` text COLLATE utf8mb4_unicode_ci COMMENT 'legacy plain text content, mainly used for text message',
  MODIFY COLUMN `image_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy image media id, kept for compatibility',
  MODIFY COLUMN `file_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy file media id, kept for compatibility',
  MODIFY COLUMN `audio_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy voice media id, kept for compatibility',
  MODIFY COLUMN `video_media_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'legacy video media id, kept for compatibility',
  MODIFY COLUMN `payload_version` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT 'v1' COMMENT 'rich payload schema version',
  MODIFY COLUMN `payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'normalized rich payload json, aligned with MassTaskCreateRequest.payload',
  MODIFY COLUMN `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  MODIFY COLUMN `template_variables_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'template variable snapshot json at task creation time',
  MODIFY COLUMN `create_time` datetime NOT NULL COMMENT 'create time',
  MODIFY COLUMN `update_time` datetime DEFAULT NULL COMMENT 'update time',
  MODIFY COLUMN `send_time` datetime DEFAULT NULL COMMENT 'planned send time',
  MODIFY COLUMN `send_status` tinyint NOT NULL DEFAULT '0' COMMENT '0=pending, 1=sending, 2=finished, 3=cancelled, 4=failed',
  MODIFY COLUMN `fail_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'task level failure reason',
  MODIFY COLUMN `creator` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'creator',
  MODIFY COLUMN `total_count` int DEFAULT '0' COMMENT 'total receivers',
  MODIFY COLUMN `sent_count` int DEFAULT '0' COMMENT 'sent receivers',
  MODIFY COLUMN `success_count` int DEFAULT '0' COMMENT 'successful receivers',
  MODIFY COLUMN `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'remark';

ALTER TABLE `mass_task_detail`
  MODIFY COLUMN `task_id` bigint NOT NULL COMMENT 'mass task id',
  MODIFY COLUMN `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  MODIFY COLUMN `receiver_id` bigint NOT NULL COMMENT 'receiver id',
  MODIFY COLUMN `receiver_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'receiver name',
  MODIFY COLUMN `message_snapshot_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'rendered payload snapshot for this receiver',
  MODIFY COLUMN `is_sent` tinyint NOT NULL DEFAULT '0' COMMENT '0=not sent, 1=sent',
  MODIFY COLUMN `planned_send_time` datetime DEFAULT NULL COMMENT 'planned send time within daily window',
  MODIFY COLUMN `send_time` datetime DEFAULT NULL COMMENT 'actual send time',
  MODIFY COLUMN `send_status` tinyint DEFAULT '0' COMMENT '0=failed, 1=success',
  MODIFY COLUMN `send_result` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'send result summary',
  MODIFY COLUMN `downstream_response` longtext COLLATE utf8mb4_unicode_ci COMMENT 'raw downstream response body if needed for troubleshooting',
  MODIFY COLUMN `create_time` datetime NOT NULL COMMENT 'create time',
  MODIFY COLUMN `update_time` datetime DEFAULT NULL COMMENT 'update time';

ALTER TABLE `mass_task_plan`
  MODIFY COLUMN `plan_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'plan name',
  MODIFY COLUMN `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  MODIFY COLUMN `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  MODIFY COLUMN `receiver_ids_json` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'receiver ids json array',
  MODIFY COLUMN `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  MODIFY COLUMN `payload_version` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  MODIFY COLUMN `payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'normalized rich payload json',
  MODIFY COLUMN `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  MODIFY COLUMN `creator` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'creator',
  MODIFY COLUMN `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'remark',
  MODIFY COLUMN `schedule_type` tinyint NOT NULL COMMENT '1=once, 2=daily, 3=weekly, 4=monthly',
  MODIFY COLUMN `schedule_rule_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'weekly/monthly rule json',
  MODIFY COLUMN `effective_start_at` datetime NOT NULL COMMENT 'effective start time',
  MODIFY COLUMN `effective_end_at` datetime DEFAULT NULL COMMENT 'effective end time',
  MODIFY COLUMN `window_start_time` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'daily send window start time, HH:mm:ss',
  MODIFY COLUMN `window_end_time` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'daily send window end time, HH:mm:ss',
  MODIFY COLUMN `rate_per_minute` int NOT NULL DEFAULT '10' COMMENT 'max receiver count per minute',
  MODIFY COLUMN `timezone` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Asia/Shanghai' COMMENT 'plan timezone',
  MODIFY COLUMN `next_trigger_time` datetime DEFAULT NULL COMMENT 'next plan trigger time',
  MODIFY COLUMN `last_trigger_time` datetime DEFAULT NULL COMMENT 'last materialized trigger time',
  MODIFY COLUMN `plan_status` tinyint NOT NULL DEFAULT '1' COMMENT '1=enabled, 2=paused, 3=cancelled, 4=finished',
  MODIFY COLUMN `create_time` datetime NOT NULL COMMENT 'create time',
  MODIFY COLUMN `update_time` datetime DEFAULT NULL COMMENT 'update time';

ALTER TABLE `message_template`
  MODIFY COLUMN `template_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'template name',
  MODIFY COLUMN `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  MODIFY COLUMN `template_content` text COLLATE utf8mb4_unicode_ci COMMENT 'legacy text template content, mainly used for text template',
  MODIFY COLUMN `payload_version` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  MODIFY COLUMN `payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'rich template payload json',
  MODIFY COLUMN `variables` longtext COLLATE utf8mb4_unicode_ci COMMENT 'template variable definition json',
  MODIFY COLUMN `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'remark',
  MODIFY COLUMN `create_time` datetime NOT NULL COMMENT 'create time',
  MODIFY COLUMN `update_time` datetime DEFAULT NULL COMMENT 'update time',
  MODIFY COLUMN `creator` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'creator';

DROP PROCEDURE IF EXISTS `ensure_idx`;
DROP PROCEDURE IF EXISTS `ensure_column`;
DELIMITER $$
CREATE PROCEDURE `ensure_column`(
  IN p_table_name varchar(64),
  IN p_column_name varchar(64),
  IN p_column_sql text
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = p_table_name
      AND column_name = p_column_name
  ) THEN
    SET @ddl = CONCAT('ALTER TABLE `', p_table_name, '` ADD COLUMN ', p_column_sql);
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END $$
DELIMITER ;

CALL `ensure_column`('mass_task', 'plan_id', '`plan_id` bigint DEFAULT NULL COMMENT ''mass task plan id'' AFTER `task_name`');
CALL `ensure_column`('mass_task', 'schedule_slot', '`schedule_slot` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ''plan execution slot key, such as 20260419090000'' AFTER `plan_id`');
CALL `ensure_column`('mass_task', 'msg_type', '`msg_type` tinyint NOT NULL DEFAULT ''0'' COMMENT ''0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app'' AFTER `task_type`');
CALL `ensure_column`('mass_task', 'payload_version', '`payload_version` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT ''v1'' COMMENT ''rich payload schema version'' AFTER `video_media_id`');
CALL `ensure_column`('mass_task', 'payload_json', '`payload_json` longtext COLLATE utf8mb4_unicode_ci COMMENT ''normalized rich payload json, aligned with MassTaskCreateRequest.payload'' AFTER `payload_version`');
CALL `ensure_column`('mass_task', 'template_id', '`template_id` bigint DEFAULT NULL COMMENT ''message template id'' AFTER `payload_json`');
CALL `ensure_column`('mass_task', 'template_variables_json', '`template_variables_json` longtext COLLATE utf8mb4_unicode_ci COMMENT ''template variable snapshot json at task creation time'' AFTER `template_id`');
CALL `ensure_column`('mass_task', 'update_time', '`update_time` datetime DEFAULT NULL COMMENT ''update time'' AFTER `create_time`');
CALL `ensure_column`('mass_task', 'fail_reason', '`fail_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ''task level failure reason'' AFTER `send_status`');

CALL `ensure_column`('mass_task_detail', 'message_snapshot_json', '`message_snapshot_json` longtext COLLATE utf8mb4_unicode_ci COMMENT ''rendered payload snapshot for this receiver'' AFTER `receiver_name`');
CALL `ensure_column`('mass_task_detail', 'planned_send_time', '`planned_send_time` datetime DEFAULT NULL COMMENT ''planned send time within daily window'' AFTER `is_sent`');
CALL `ensure_column`('mass_task_detail', 'downstream_response', '`downstream_response` longtext COLLATE utf8mb4_unicode_ci COMMENT ''raw downstream response body if needed for troubleshooting'' AFTER `send_result`');

CALL `ensure_column`('wx_user_info', 'last_pulled_message_id', '`last_pulled_message_id` bigint DEFAULT NULL COMMENT ''上次拉取的最大消息ID'' AFTER `is_del`');

ALTER TABLE `wx_user_info`
  MODIFY COLUMN `last_pulled_message_id` bigint DEFAULT NULL COMMENT '上次拉取的最大消息ID';

DELIMITER $$
CREATE PROCEDURE `ensure_idx`(
  IN p_table_name varchar(64),
  IN p_index_name varchar(64),
  IN p_index_sql text
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = p_table_name
      AND index_name = p_index_name
  ) THEN
    SET @ddl = p_index_sql;
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END $$
DELIMITER ;

CALL `ensure_idx`('mass_task', 'idx_creator', 'ALTER TABLE `mass_task` ADD KEY `idx_creator` (`creator`)');
CALL `ensure_idx`('mass_task', 'idx_task_type', 'ALTER TABLE `mass_task` ADD KEY `idx_task_type` (`task_type`)');
CALL `ensure_idx`('mass_task', 'idx_msg_type', 'ALTER TABLE `mass_task` ADD KEY `idx_msg_type` (`msg_type`)');
CALL `ensure_idx`('mass_task', 'idx_send_status', 'ALTER TABLE `mass_task` ADD KEY `idx_send_status` (`send_status`)');
CALL `ensure_idx`('mass_task', 'idx_send_time', 'ALTER TABLE `mass_task` ADD KEY `idx_send_time` (`send_time`)');
CALL `ensure_idx`('mass_task', 'idx_create_time', 'ALTER TABLE `mass_task` ADD KEY `idx_create_time` (`create_time`)');
CALL `ensure_idx`('mass_task', 'idx_template_id', 'ALTER TABLE `mass_task` ADD KEY `idx_template_id` (`template_id`)');
CALL `ensure_idx`('mass_task', 'idx_plan_id', 'ALTER TABLE `mass_task` ADD KEY `idx_plan_id` (`plan_id`)');
CALL `ensure_idx`('mass_task', 'idx_schedule_slot', 'ALTER TABLE `mass_task` ADD KEY `idx_schedule_slot` (`schedule_slot`)');

CALL `ensure_idx`('mass_task_detail', 'idx_task_id', 'ALTER TABLE `mass_task_detail` ADD KEY `idx_task_id` (`task_id`)');
CALL `ensure_idx`('mass_task_detail', 'idx_receiver_id', 'ALTER TABLE `mass_task_detail` ADD KEY `idx_receiver_id` (`receiver_id`)');
CALL `ensure_idx`('mass_task_detail', 'idx_is_sent', 'ALTER TABLE `mass_task_detail` ADD KEY `idx_is_sent` (`is_sent`)');
CALL `ensure_idx`('mass_task_detail', 'idx_send_status', 'ALTER TABLE `mass_task_detail` ADD KEY `idx_send_status` (`send_status`)');
CALL `ensure_idx`('mass_task_detail', 'idx_planned_send_time', 'ALTER TABLE `mass_task_detail` ADD KEY `idx_planned_send_time` (`planned_send_time`)');

CALL `ensure_idx`('mass_task_plan', 'idx_plan_status', 'ALTER TABLE `mass_task_plan` ADD KEY `idx_plan_status` (`plan_status`)');
CALL `ensure_idx`('mass_task_plan', 'idx_next_trigger_time', 'ALTER TABLE `mass_task_plan` ADD KEY `idx_next_trigger_time` (`next_trigger_time`)');
CALL `ensure_idx`('mass_task_plan', 'idx_effective_start_at', 'ALTER TABLE `mass_task_plan` ADD KEY `idx_effective_start_at` (`effective_start_at`)');

CALL `ensure_idx`('message_template', 'idx_creator', 'ALTER TABLE `message_template` ADD KEY `idx_creator` (`creator`)');
CALL `ensure_idx`('message_template', 'idx_msg_type', 'ALTER TABLE `message_template` ADD KEY `idx_msg_type` (`msg_type`)');
CALL `ensure_idx`('message_template', 'idx_create_time', 'ALTER TABLE `message_template` ADD KEY `idx_create_time` (`create_time`)');

DROP PROCEDURE IF EXISTS `ensure_fk`;
DELIMITER $$
CREATE PROCEDURE `ensure_fk`()
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.referential_constraints
    WHERE constraint_schema = DATABASE()
      AND table_name = 'mass_task_detail'
      AND constraint_name = 'fk_mass_task_detail_task_id'
  ) THEN
    ALTER TABLE `mass_task_detail`
      ADD CONSTRAINT `fk_mass_task_detail_task_id`
      FOREIGN KEY (`task_id`) REFERENCES `mass_task` (`id`) ON DELETE CASCADE;
  END IF;
END $$
DELIMITER ;

CALL `ensure_fk`();

DROP PROCEDURE IF EXISTS `ensure_fk`;
DROP PROCEDURE IF EXISTS `ensure_idx`;
DROP PROCEDURE IF EXISTS `ensure_column`;
