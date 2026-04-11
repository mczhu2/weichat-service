CREATE TABLE `mass_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `task_name` varchar(255) NOT NULL COMMENT 'task name',
  `task_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=user mass send, 2=group mass send',
  `content` text COMMENT 'text content',
  `image_media_id` varchar(255) DEFAULT NULL COMMENT 'image media id',
  `file_media_id` varchar(255) DEFAULT NULL COMMENT 'file media id',
  `audio_media_id` varchar(255) DEFAULT NULL COMMENT 'audio media id',
  `video_media_id` varchar(255) DEFAULT NULL COMMENT 'video media id',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=audio, 4=video',
  `template_id` bigint DEFAULT NULL COMMENT 'message template id',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `send_time` datetime DEFAULT NULL COMMENT 'send time',
  `send_status` tinyint NOT NULL DEFAULT '0' COMMENT '0=pending, 1=sending, 2=finished, 3=cancelled',
  `creator` varchar(100) DEFAULT NULL COMMENT 'creator',
  `total_count` int DEFAULT '0' COMMENT 'total receivers',
  `sent_count` int DEFAULT '0' COMMENT 'sent receivers',
  `success_count` int DEFAULT '0' COMMENT 'successful receivers',
  `remark` text COMMENT 'remark',
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`),
  KEY `idx_send_status` (`send_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task';

CREATE TABLE `mass_task_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `task_id` bigint NOT NULL COMMENT 'mass task id',
  `receiver_type` tinyint NOT NULL DEFAULT '1' COMMENT '1=external contact, 2=group',
  `receiver_id` bigint NOT NULL COMMENT 'receiver id',
  `receiver_name` varchar(255) DEFAULT NULL COMMENT 'receiver name',
  `is_sent` tinyint NOT NULL DEFAULT '0' COMMENT '0=not sent, 1=sent',
  `send_time` datetime DEFAULT NULL COMMENT 'send time',
  `send_status` tinyint DEFAULT '0' COMMENT '0=failed, 1=success',
  `send_result` varchar(500) DEFAULT NULL COMMENT 'send result',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_receiver_id` (`receiver_id`),
  KEY `idx_is_sent` (`is_sent`),
  KEY `idx_send_status` (`send_status`),
  CONSTRAINT `fk_mass_task_detail_task_id`
    FOREIGN KEY (`task_id`) REFERENCES `mass_task` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='mass task detail';

CREATE TABLE `message_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `template_name` varchar(255) NOT NULL COMMENT 'template name',
  `template_content` text NOT NULL COMMENT 'template content',
  `variables` text COMMENT 'template variables json',
  `msg_type` tinyint NOT NULL DEFAULT '0' COMMENT '0=text, 1=image, 2=file, 3=audio, 4=video',
  `create_time` datetime NOT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  `creator` varchar(100) DEFAULT NULL COMMENT 'creator',
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='message template';
