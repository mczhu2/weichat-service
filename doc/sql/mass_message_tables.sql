-- Recommended complete schema for mass task/message template.
-- Goal:
-- 1. Keep legacy media-id fields for compatibility.
-- 2. Add payload_json to support rich message types:
--    0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app
-- 3. Reserve template variable snapshot and task failure diagnostics.

CREATE TABLE `mass_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `task_name` varchar(255) NOT NULL COMMENT 'task name',
  `plan_id` bigint DEFAULT NULL COMMENT 'mass task plan id',
  `schedule_slot` varchar(32) DEFAULT NULL COMMENT 'plan execution slot key, such as 20260419090000',
  `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  `content` text COMMENT 'legacy plain text content, mainly used for text message',
  `image_media_id` varchar(255) DEFAULT NULL COMMENT 'legacy image media id, kept for compatibility',
  `file_media_id` varchar(255) DEFAULT NULL COMMENT 'legacy file media id, kept for compatibility',
  `audio_media_id` varchar(255) DEFAULT NULL COMMENT 'legacy voice media id, kept for compatibility',
  `video_media_id` varchar(255) DEFAULT NULL COMMENT 'legacy video media id, kept for compatibility',
  `payload_version` varchar(32) NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  `payload_json` longtext COMMENT 'normalized rich payload json, aligned with MassTaskCreateRequest.payload',
  `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  `template_variables_json` longtext COMMENT 'template variable snapshot json at task creation time',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  `send_time` datetime DEFAULT NULL COMMENT 'planned send time',
  `send_status` tinyint NOT NULL DEFAULT '0' COMMENT '0=pending, 1=sending, 2=finished, 3=cancelled, 4=failed',
  `fail_reason` varchar(500) DEFAULT NULL COMMENT 'task level failure reason',
  `creator` varchar(100) DEFAULT NULL COMMENT 'creator',
  `total_count` int DEFAULT '0' COMMENT 'total receivers',
  `sent_count` int DEFAULT '0' COMMENT 'sent receivers',
  `success_count` int DEFAULT '0' COMMENT 'successful receivers',
  `remark` text COMMENT 'remark',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_schedule_slot` (`schedule_slot`),
  KEY `idx_creator` (`creator`),
  KEY `idx_task_type` (`task_type`),
  KEY `idx_msg_type` (`msg_type`),
  KEY `idx_send_status` (`send_status`),
  KEY `idx_send_time` (`send_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task';

CREATE TABLE `mass_task_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `task_id` bigint NOT NULL COMMENT 'mass task id',
  `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  `receiver_id` bigint NOT NULL COMMENT 'receiver id',
  `receiver_name` varchar(255) DEFAULT NULL COMMENT 'receiver name',
  `message_snapshot_json` longtext COMMENT 'rendered payload snapshot for this receiver',
  `is_sent` tinyint NOT NULL DEFAULT '0' COMMENT '0=not sent, 1=sent',
  `planned_send_time` datetime DEFAULT NULL COMMENT 'planned send time within daily window',
  `send_time` datetime DEFAULT NULL COMMENT 'actual send time',
  `send_status` tinyint DEFAULT '0' COMMENT '0=failed, 1=success',
  `send_result` varchar(500) DEFAULT NULL COMMENT 'send result summary',
  `downstream_response` longtext COMMENT 'raw downstream response body if needed for troubleshooting',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_receiver_id` (`receiver_id`),
  KEY `idx_is_sent` (`is_sent`),
  KEY `idx_planned_send_time` (`planned_send_time`),
  KEY `idx_send_status` (`send_status`),
  CONSTRAINT `fk_mass_task_detail_task_id`
    FOREIGN KEY (`task_id`) REFERENCES `mass_task` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task detail';

CREATE TABLE `mass_task_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `plan_name` varchar(255) NOT NULL COMMENT 'plan name',
  `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  `receiver_ids_json` longtext NOT NULL COMMENT 'receiver ids json array',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  `payload_version` varchar(32) NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  `payload_json` longtext COMMENT 'normalized rich payload json',
  `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  `creator` varchar(100) DEFAULT NULL COMMENT 'creator',
  `remark` text COMMENT 'remark',
  `schedule_type` tinyint NOT NULL COMMENT '1=once, 2=daily, 3=weekly, 4=monthly',
  `schedule_rule_json` longtext COMMENT 'weekly/monthly rule json',
  `effective_start_at` datetime NOT NULL COMMENT 'effective start time',
  `effective_end_at` datetime DEFAULT NULL COMMENT 'effective end time',
  `window_start_time` varchar(16) NOT NULL COMMENT 'daily send window start time, HH:mm:ss',
  `window_end_time` varchar(16) NOT NULL COMMENT 'daily send window end time, HH:mm:ss',
  `rate_per_minute` int NOT NULL DEFAULT '10' COMMENT 'max receiver count per minute',
  `timezone` varchar(64) NOT NULL DEFAULT 'Asia/Shanghai' COMMENT 'plan timezone',
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

CREATE TABLE `message_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `template_name` varchar(255) NOT NULL COMMENT 'template name',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=voice, 4=video, 5=link, 6=app',
  `template_content` text COMMENT 'legacy text template content, mainly used for text template',
  `payload_version` varchar(32) NOT NULL DEFAULT 'v1' COMMENT 'rich payload schema version',
  `payload_json` longtext COMMENT 'rich template payload json',
  `variables` longtext COMMENT 'template variable definition json',
  `remark` text COMMENT 'remark',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  `creator` varchar(100) DEFAULT NULL COMMENT 'creator',
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`),
  KEY `idx_msg_type` (`msg_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='message template';

-- Suggested payload_json examples:
--
-- text:
-- {"content":"hello"}
--
-- image/file/voice/video:
-- {"media":{"url":"https://...","base64":"...","filename":"demo.mp3","contentType":"audio/mpeg","voiceTime":4}}
--
-- link:
-- {"link":{"url":"https://www.baidu.com","title":"title","content":"desc","imgurl":"https://..." }}
--
-- app:
-- {"app":{"desc":"desc","appName":"mini app","title":"title","pagepath":"pages/index","username":"gh_xxx@app","appid":"wx123","weappIconUrl":"https://...","cover":{"url":"https://...","base64":"..."}}}
