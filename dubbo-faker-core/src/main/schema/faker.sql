CREATE TABLE `method_invoke` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `app_name` varchar(100) DEFAULT NULL,
  `class_name` varchar(255) DEFAULT NULL,
  `method_name` varchar(255) DEFAULT NULL,
  `param_type` text,
  `return_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `invoke_param` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `type` varchar(11) DEFAULT NULL,
  `param_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `faker_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `faker_id` varchar(32) DEFAULT NULL,
  `invoke_id` int(11) DEFAULT NULL,
  `real_param` varchar(255) DEFAULT NULL,
  `code` tinyint(3) unsigned DEFAULT NULL,
  `message` varchar(200) DEFAULT NULL,
  `spend_time` int(11) DEFAULT NULL,
  `invoke_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;