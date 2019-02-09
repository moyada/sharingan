INSERT INTO `app_info` (`id`, `name`, `group_id`, `artifact_id`, `version`, `url`, `dependencies`, `internal`)
VALUES
	(1, 'dubbo-test', 'cn.moyada', 'dubbo-test-api', NULL, NULL, NULL, 0),
	(2, 'springCloudServer', 'cn.moyada', 'spring-cloud-service-api', NULL, NULL, NULL, 0);

INSERT INTO `service_info` (`id`, `app_id`, `name`, `protocol`, `class_name`)
VALUES
	(1, 1, 'UserService', 'dubbo', NULL, 'cn.moyada.dubbo.test.api.UserService'),
	(2, 2, 'TestService', 'springcloud', 'HTTP', NULL);


INSERT INTO `function_info` (`id`, `app_id`, `service_id`, `class_name`, `method_name`, `param_type`, `return_type`, `expression`)
VALUES
	(1, 1, 1, 'cn.moyada.dubbo.test.api.UserService', 'getById', 'java.lang.Integer', 'cn.moyada.dubbo.test.api.UserInfo', '[\"${dubbo-test.id}\"]'),
	(2, 1, 1, 'cn.moyada.dubbo.test.api.UserService', 'save', 'cn.moyada.dubbo.test.api.UserInfo', 'void', '[\"{\'userId\': \'#{int.random}\', \'nikeName\': \'test#{double[2].-50.5--20.50}\', \'age\': \'#{int.15-40}\', \'gender\': \'true\', \'phone\': \'+86 1537751317374\'}\"]'),
	(3, 1, 1, 'cn.moyada.dubbo.test.api.UserService', 'queryAll', NULL, 'java.util.List', '[]'),
	(4, 1, 1, 'cn.moyada.dubbo.test.api.UserService', 'queryByIds', 'java.util.List', 'java.util.List', '[\"[#{int.20-50}, #{int.20-50}, #{int.20-50}]\"]');

INSERT INTO `http_info` (`id`, `app_id`, `service_id`, `method_name`, `method_type`, `param`, `header`, `expression`)
VALUES
	(1, 2, 2, '/{id}', 'GET', NULL, NULL, '{\"param\": {\"id\": \"#{int.random}\"}}'),
	(2, 2, 2, '/info', 'GET', 'key', NULL, '{\"param\": {\"key\": \"test\"}}');

INSERT INTO `invoke_data` (`app_id`, `domain`, `param_value`, `date_create`)
VALUES
	(1, 'id', '1', NULL),
	(1, 'id', '12', NULL),
	(1, 'id', '14', NULL),
	(1, 'id', '143', NULL),
	(1, 'id', '145', NULL),
	(1, 'id', '146', NULL),
	(1, 'id', '147', NULL),
	(1, 'id', '15', NULL),
	(1, 'id', '153', NULL),
	(1, 'id', '154', NULL),
	(1, 'id', '156', NULL),
	(1, 'id', '157', NULL),
	(1, 'id', '161', NULL),
	(1, 'id', '163', NULL),
	(1, 'id', '164', NULL),
	(1, 'id', '173', NULL),
	(1, 'id', '174', NULL),
	(1, 'id', '19', NULL),
	(1, 'id', '195', NULL),
	(1, 'id', '21', NULL),
	(1, 'id', '22', NULL),
	(1, 'id', '295', NULL),
	(1, 'id', '3', NULL),
	(1, 'id', '31', NULL),
	(1, 'id', '32', NULL),
	(1, 'id', '4', NULL),
	(1, 'id', '43', NULL),
	(1, 'id', '45', NULL),
	(1, 'id', '46', NULL),
	(1, 'id', '47', NULL),
	(1, 'id', '5', NULL),
	(1, 'id', '54', NULL),
	(1, 'id', '56', NULL);
