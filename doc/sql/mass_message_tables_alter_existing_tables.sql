-- Existing table alter script for current project.
-- Execute this file when the original mass_task / mass_task_detail tables already exist.

ALTER TABLE `mass_task`
  ADD COLUMN `plan_id` bigint DEFAULT NULL COMMENT 'mass task plan id' AFTER `task_name`,
  ADD COLUMN `schedule_slot` varchar(32) DEFAULT NULL COMMENT 'plan execution slot key, such as 20260419090000' AFTER `plan_id`,
  ADD KEY `idx_plan_id` (`plan_id`),
  ADD KEY `idx_schedule_slot` (`schedule_slot`);

ALTER TABLE `mass_task_detail`
  ADD COLUMN `planned_send_time` datetime DEFAULT NULL COMMENT 'planned send time within daily window' AFTER `is_sent`,
  ADD KEY `idx_planned_send_time` (`planned_send_time`);
