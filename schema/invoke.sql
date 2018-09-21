CREATE TABLE `invoke_param` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) unsigned NOT NULL COMMENT '项目编号',
  `domain` varchar(11) NOT NULL COMMENT '参数领域',
  `param_value` varchar(255) NOT NULL COMMENT '参数值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param` (`app_id`,`type`,`param_value`),
  KEY `idx_type` (`app_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参数表达式映射表';

CREATE TABLE `invocation_report` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `faker_id` int(11) NOT NULL COMMENT '测试序号',
  `app_id` int(11) unsigned NOT NULL COMMENT '项目编号',
  `service_id` int(11) unsigned NOT NULL COMMENT '服务编号',
  `func_id` int(11) unsigned NOT NULL COMMENT '调用方法编号',
  `total_invoke` int(11) unsigned DEFAULT NULL COMMENT '请求次数',
  `response_invoke` int(11) unsigned DEFAULT NULL COMMENT '响应次数',
  `min_response_time` int(11) unsigned DEFAULT NULL COMMENT '最小耗时',
  `max_response_time` int(11) unsigned DEFAULT NULL COMMENT '最大耗时',
  `avg_response_time` int(11) unsigned DEFAULT NULL COMMENT '平均耗时',
  `data_create` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id` (`faker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调用报告表';

CREATE TABLE `invocation_result` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `faker_id` varchar(32) NOT NULL COMMENT '测试序号',
  `real_args` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '实际请求参数',
  `code` smallint(3) unsigned DEFAULT NULL COMMENT '请求结果码',
  `result` text COMMENT '请求返回信息',
  `error_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '异常信息',
  `response_time` int(11) unsigned DEFAULT NULL COMMENT '耗时',
  `invoke_time` timestamp NULL DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `idx_log` (`faker_id`,`code`,`response_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调用结果表';