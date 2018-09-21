CREATE TABLE `app_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '项目编号',
  `name` varchar(100) NOT NULL COMMENT '项目名',
  `group_id` varchar(50) NOT NULL COMMENT '依赖分组',
  `artifact_id` varchar(50) NOT NULL COMMENT '依赖名',
  `version` varchar(50) DEFAULT NULL COMMENT '版本号',
  `url` varchar(255) DEFAULT NULL COMMENT '直接链接',
  `dependencies` varchar(100) DEFAULT NULL COMMENT '包含依赖,关联dependency表',
  `internal` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否内部依赖，1 -> 是，不在页面展示范围',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app` (`name`),
  UNIQUE KEY `uk_domain` (`group_id`,`artifact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目信息表';

CREATE TABLE `service_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) unsigned NOT NULL COMMENT '项目编号',
  `name` varchar(100) NOT NULL COMMENT '服务名称',
  `protocol` enum('dubbo') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'rpc协议',
  `class_name` varchar(255) DEFAULT NULL COMMENT '接口类名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_service` (`app_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务信息表';

CREATE TABLE `function_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) unsigned DEFAULT NULL COMMENT '项目编号',
  `service_id` int(11) unsigned DEFAULT NULL COMMENT '服务编号',
  `class_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `param_type` varchar(255) DEFAULT NULL COMMENT '参数类型以,分隔',
  `return_type` varchar(255) DEFAULT NULL COMMENT '返回值类型',
  `expression` varchar(255) DEFAULT NULL COMMENT '默认参数表达式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_func` (`app_id`, `service_id`,`class_name`,`method_name`,`param_type`,`return_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方法信息表';