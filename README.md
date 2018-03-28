# dubbo-faker

dubbo-faker，可以用来对`dubbo`项目进行简单测试。

通过预设的参数，并发数，QPS直接通过dubbo协议连接测试生产者接口，并生成测试报告。

## 如何使用

### 1. `clone`代码

```sh
git clone git@github.com:moyada/dubbo-faker.git
```

### 2. 编译安装

```sh
cd dubbo-faker
mvn clean package -Dmaven.test.skip=true  
```

### 3. 修改`pom.xml`、`jdbc.properties`、`dubbo.properties`、`application-dubbo-import.xml`配置文件

* 在`pom.xml`增加测试目标的`dubbo`服务方依赖:

```xml
<dependency>
    <groupId>com.company</groupId>
    <artifactId>project</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
 ```
 
* 修改`jdbc.properties`中mysql数据库连接配置

* 修改`dubbo.properties`中zookeeper连接配置

* 修改`application-dubbo-import.xml`增加测试接口引用

```xml
<dubbo:reference id="dubboService" interface="com.company.project.DubboService" />
```

### 4. 执行 `schema/faker.sql` 创建数据库表结构


### 5. 导入测试参数

```sql
INSERT INTO `method_invoke` (`id`, `app_id`, `app_name`, `class_name`, `method_name`, `param_type`, `return_type`, `expression`)
VALUES
	(1, 1, 'test', 'com.company.project.DubboService', 'getListByNameAndType', 'java.lang.String,java.lang.Integer', 'java.util.List', '["${1.param}"]');


INSERT INTO `invoke_param` (`id`, `app_id`, `type`, `param_value`)
VALUES
	(1, 1, 'param', '12345');
```


### 6. 启动项目，打开 http://localhost:8080/index.html 进入管理页面，测试请求

接口地址: http://localhost:8080/swagger-ui.html

invokeId 输入`method_invoke`的主键(如1)

invokeExpression 支持输入固定参数或参数表达式，需以`json`数组的格式(如["${1.test}"]、["12345"])

表达式格式为`${app_id.type}`，程序将会从invoke_param数据中获取模拟参数随机抽取调用，当使用了表达式而又无模拟参数时将抛出`InitializeInvokerException`

测试结果保存在`faker_log`表中，每次测试将生成一个唯一的`faker_id`，完成时打印日志信息`logging shutdown: {faker_id}`，并弹窗提示。






