CREATE TABLE `app_info` (
  `app_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '项目编号',
  `app_name` varchar(100) DEFAULT NULL COMMENT '项目名',
  `group_id` varchar(50) DEFAULT NULL COMMENT '依赖分组',
  `artifact_id` varchar(50) DEFAULT NULL COMMENT '依赖名',
  `version` varchar(50) DEFAULT NULL COMMENT '版本号',
  `url` varchar(255) DEFAULT NULL COMMENT '直接链接',
  `dependencies` varchar(100) DEFAULT NULL COMMENT '包含依赖,关联dependency表',
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='项目信息表';

CREATE TABLE `dependency` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '依赖编号',
  `app_name` varchar(100) DEFAULT NULL COMMENT '依赖名',
  `group_id` varchar(50) DEFAULT NULL COMMENT '依赖分组',
  `artifact_id` varchar(50) DEFAULT NULL COMMENT '依赖名',
  `version` varchar(50) DEFAULT NULL COMMENT '版本号',
  `url` varchar(255) DEFAULT NULL COMMENT '直接链接'
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='依赖表';

CREATE TABLE `method_invoke` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) DEFAULT NULL COMMENT '项目编号',
  `app_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `class_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `param_type` varchar(255) DEFAULT NULL COMMENT '参数类型以,分隔',
  `return_type` varchar(255) DEFAULT NULL COMMENT '返回值类型',
  `expression` varchar(255) DEFAULT NULL COMMENT '默认参数表达式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_method` (`app_id`,`class_name`,`method_name`,`param_type`,`return_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='调用方法表';


CREATE TABLE `invoke_param` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) DEFAULT NULL COMMENT '项目编号',
  `type` varchar(11) DEFAULT NULL COMMENT '参数类别',
  `param_value` varchar(255) DEFAULT NULL COMMENT '参数值',
  `date_create` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param` (`app_id`,`type`,`param_value`),
  KEY `idx_type` (`app_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='调用参数表';


CREATE TABLE `faker_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `faker_id` varchar(32) DEFAULT NULL COMMENT '测试序号',
  `invoke_id` int(11) DEFAULT NULL COMMENT '调用方法编号',
  `real_param` varchar(255) DEFAULT NULL COMMENT '实际请求参数',
  `code` smallint(3) unsigned DEFAULT NULL COMMENT '请求结果码',
  `result` text COMMENT '请求返回信息',
  `message` text COMMENT '失败异常',
  `spend_time` int(11) DEFAULT NULL COMMENT '耗时',
  `invoke_time` timestamp NULL DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `idx_log` (`faker_id`,`code`,`spend_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='调用结果表';
