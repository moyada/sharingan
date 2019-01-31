CREATE TABLE `invoke_data` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) unsigned DEFAULT NULL COMMENT '项目编号',
  `domain` varchar(11) DEFAULT NULL COMMENT '参数类别',
  `param_value` varchar(191) DEFAULT NULL COMMENT '参数值',
  `date_create` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param` (`app_id`,`domain`,`param_value`),
  KEY `idx_type` (`app_id`,`domain`)
) COMMENT='调用参数表';

CREATE TABLE `invoke_report` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `report_id` varchar(32) NOT NULL DEFAULT '' COMMENT '测试序号',
  `app_id` int(11) unsigned NOT NULL COMMENT '项目编号',
  `service_id` int(11) unsigned NOT NULL COMMENT '服务编号',
  `func_id` int(11) unsigned NOT NULL COMMENT '调用方法编号',
  `total_invoke` int(11) unsigned DEFAULT NULL COMMENT '请求次数',
  `response_invoke` int(11) unsigned DEFAULT NULL COMMENT '响应次数',
  `success_rate` double DEFAULT NULL COMMENT '成功率',
  `min_response_time` int(11) unsigned DEFAULT NULL COMMENT '最小耗时',
  `max_response_time` int(11) unsigned DEFAULT NULL COMMENT '最大耗时',
  `avg_response_time` int(11) unsigned DEFAULT NULL COMMENT '平均耗时',
  `date_create` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id` (`report_id`)
) COMMENT='调用报告表';

CREATE TABLE `invoke_result` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `report_id` int(11) unsigned NOT NULL COMMENT '测试序号',
  `real_args` varchar(191) DEFAULT NULL COMMENT '实际请求参数',
  `code` smallint(3) unsigned DEFAULT NULL COMMENT '请求结果码',
  `result` text COMMENT '请求返回信息',
  `error_msg` text COMMENT '异常信息',
  `response_time` int(11) unsigned DEFAULT NULL COMMENT '耗时',
  `invoke_time` timestamp NULL DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `idx_log` (`report_id`,`code`,`response_time`)
) COMMENT='调用结果表';