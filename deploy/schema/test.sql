INSERT INTO `app_info` (`id`, `name`, `group_id`, `artifact_id`, `version`, `url`, `dependencies`, `internal`)
VALUES
	(1, 'dubbo-test', 'io.moyada', 'dubbo-test-api', NULL, NULL, NULL, 0),
	(2, 'springCloudServer', 'io.moyada', 'spring-cloud-service-api', NULL, NULL, NULL, 0);


INSERT INTO `service_info` (`id`, `app_id`, `name`, `protocol`, `class_name`)
VALUES
	(1, 1, 'UserService', 'Dubbo', 'io.moyada.dubbo.test.api.UserService'),
	(2, 2, 'TestService', 'SpringCloud', NULL);

INSERT INTO `function_info` (`id`, `app_id`, `service_id`, `class_name`, `method_name`, `param_type`, `return_type`, `expression`)
VALUES
	(1, 1, 1, 'io.moyada.rpc.demo.dubbo.api.UserService', 'getById', 'java.lang.Integer', 'io.moyada.rpc.demo.dubbo.api.UserInfo', '[\"${dubbo-test.id}\"]'),
	(2, 1, 1, 'io.moyada.rpc.demo.dubbo.api.UserService', 'save', 'io.moyada.rpc.demo.dubbo.api.UserInfo', 'io.moyada.rpc.demo.dubbo.api.Result', '[\"{\'userId\': \'${test-project.uid}\', \'nikeName\': \'test#{double[2].random}\', \'age\': \'#{int.-10-240}\', \'gender\': \'true\', \'phone\': \'+86 1537751317374\'}\"]'),
	(3, 1, 1, 'io.moyada.rpc.demo.dubbo.api.UserService', 'queryAll', NULL, 'java.util.List', '[]'),
	(4, 1, 1, 'io.moyada.rpc.demo.dubbo.api.UserService', 'queryByIds', 'java.util.List', 'java.util.List', '[\"[#{int.20-50}, #{int.20-50}, #{int.20-50}]\"]');

INSERT INTO `http_info` (`id`, `app_id`, `service_id`, `method_name`, `method_type`, `param`, `header`, `body`, `content_type`)
VALUES
	(1, 2, 2, '/{id}', 'GET', '{"id": "#{int.random}"}', NULL, NULL, 'application/x-www-form-urlencoded'),
	(2, 2, 2, '/info', 'POST', '{"key": "test"}', NULL, NULL, 'application/x-www-form-urlencoded'),
	(3, 2, 2, 'getId', 'POST', null, NULL, '{"code": #{int.[100-500]},"msg":"ok", "data": "#{int.random}"}', 'application/json');

INSERT INTO `invoke_data` (`app_id`, `domain`, `param_value`, `date_create`)
VALUES
	(1, 'id', '1', NULL),
	(1, 'id', '32', NULL),
	(1, 'id', '14', NULL),
	(1, 'id', '6', NULL),
	(1, 'id', '145', NULL),
	(1, 'id', '22', NULL),
	(1, 'id', '12', NULL),
	(1, 'id', '64', NULL),
	(1, 'id', '143', NULL),
	(1, 'id', '30', NULL),
	(1, 'id', '146', NULL),
	(1, 'id', '147', NULL),
	(1, 'id', '15', NULL),
	(1, 'id', '153', NULL),
	(1, 'id', '154', NULL),
	(1, 'id', '156', NULL),
	(1, 'id', '61', NULL),
	(1, 'id', '54', NULL),
	(1, 'id', '163', NULL),
	(1, 'id', '164', NULL),
	(1, 'id', '73', NULL),
	(1, 'id', '174', NULL),
	(1, 'id', '19', NULL);