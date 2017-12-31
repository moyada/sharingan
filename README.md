# dubbo-faker

dubbo-faker，可以用来对`dubbo`项目进行简单测试。

通过预设的参数，并发数，QPS直接通过dubbo协议连接测试生产者接口，并生成测试报告。

## 如何使用

### 1. `clone`代码

```sh
git clone git@github.com:xueyikang/dubbo-faker.git
```

### 2. 编译安装

```sh
cd dubbo-faker
mvn clean install
```

### 3. 修改`maven`、`jdbc.properties`、`dubbo.properties`、`application-dubbo-import.xml`配置文件

* 在`pom.xml`增加测试目标的`dubbo`服务方依赖:

```xml
<parent>
    <groupId>com.company</groupId>
    <artifactId>project</artifactId>
    <version>1.0.0.RELEASE</version>
</parent>
 ```
 
* 修改`jdbc.properties`中mysql数据库连接配置

* 修改`dubbo.properties`中zookeeper连接配置

* 修改`application-dubbo-import.xml`增加测试接口引用

```xml
<dubbo:reference id="dubboService" interface="com.company.project.DubboService" />
```

### 4. 执行 `dubbo-faker-core/schema/faker.sql` 创建数据库表

```sql
CREATE TABLE `method_invoke` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) DEFAULT NULL COMMENT '项目编号',
  `app_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `class_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `param_type` text COMMENT '参数类型以,分隔',
  `return_type` varchar(255) DEFAULT NULL COMMENT '返回值类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='调用方法表';


CREATE TABLE `invoke_param` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) DEFAULT NULL COMMENT '项目编号',
  `type` varchar(11) DEFAULT NULL COMMENT '参数类别',
  `param_value` varchar(255) DEFAULT NULL COMMENT '参数值',
  PRIMARY KEY (`id`),
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='测试请求表';
```

### 5. 导入测试参数

```sql
INSERT INTO `method_invoke` (`id`, `app_id`, `app_name`, `class_name`, `method_name`, `param_type`, `return_type`)
VALUES
	(1, 1, 'test', 'com.company.project.DubboService', 'getListByNameAndType', 'java.lang.String,java.lang.Integer', 'java.util.List');


INSERT INTO `invoke_param` (`id`, `app_id`, `type`, `param_value`)
VALUES
	(1, 1, 'test', '12345');
```

### 6. 启动项目，打开`localhost:8080/swagger-ui.html`，测试请求

invokeId 输入`method_invoke`的主键(如1)

invokeExpression 支持输入固定参数或参数表达式，需以`json`数组的格式(如["${1.test}"]、["12345"])

表达式格式为`${app_id.type}`，程序将会从invoke_param数据中获取模拟参数随机抽取调用，当使用了表达式而又无模拟参数时将抛出`NoSuchParamException`

测试结果保存在`faker_log`表中，每次测试将生成一个唯一的`faker_id`，暂时通过日志信息`"faker invoke done:`观察测试的完成情况。






