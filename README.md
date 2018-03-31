# dubbo-faker

dubbo-faker是针对[dubbo](https://github.com/apache/incubator-dubbo)项目进行服务测试的工程，用于快速检测代码变更的正确性。

通过预设的参数表达式、qps等直接通过dubbo通信调用生产者服务，并生成测试报告。

## 如何使用

### 1. 下载项目

```sh
git clone git@github.com:moyada/dubbo-faker.git
```

### 2. 编译打包

```sh
sh build.sh
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


### 6. 启动项目，打开 http://localhost:8080/index.html 进入测试页面，测试请求

接口地址: http://localhost:8080/swagger-ui.html

invokeId 输入`method_invoke`的主键(如1)

invokeExpression 支持输入固定参数或参数表达式，需以`json`数组的格式(如["${1.test}"]、["12345"])

表达式格式为`${app_id.type}`，程序将会从invoke_param数据中获取模拟参数随机抽取调用，当使用了表达式而又无模拟参数时将抛出`InitializeInvokerException`

测试结果保存在`faker_log`表中，每次测试将生成一个唯一的`faker_id`，完成时打印日志信息`logging shutdown: {faker_id}`，并弹窗提示。



### 7. 使用拦截捕捉调用参数，具体配置可参考`dubbo-faker-filter`模块下的resources目录

#### 1. 打包`dubbo-faker-api``dubbo-faker-filter`模块，并在使用项目中引入依赖。
```xml
<dependency>
    <artifactId>dubbo-faker-api</artifactId>
    <groupId>cn.moyada</groupId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>


<dependency>
    <artifactId>dubbo-faker-filter</artifactId>
    <groupId>cn.moyada</groupId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>

```

#### 2. 增加配置文件`faker.properties`并读入系统
```properties
# 项目名称
faker.appName=test

# 是否捕获空结果调用
faker.nullable=false

# 是否过滤异常调用
faker.exception.filter=true

# 参数监听器临时空间大小
faker.capacity=500

# 拦截最大线程数
faker.maxThread=10

# 保存拦截参数间隔毫秒数
faker.interval=1000

```

#### 3. 配置所需依赖
```xml
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="mapperLocations" >
            <list>
                <value>classpath*:sqlmaps/faker.xml</value>
            </list>
        </property>
        <property name="dataSource" ref="dataSource" />
    </bean>

    <bean id="fakerMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
        <property name="mapperInterface" value="cn.moyada.dubbo.faker.filter.dao.FakerDAO"/>
        <property name="sqlSessionFactory" ref="sqlSessionFactory" />
    </bean>


    <bean id="batchRecordListener" class="cn.moyada.dubbo.faker.filter.listener.BatchRecordListener" />
    <bean id="fakerManager" class="cn.moyada.dubbo.faker.filter.manager.FakerManager" />
```

#### 4. 在资源路径下配置`META-INF/dubbo/com.alibaba.dubbo.rpc.ExporterListener`
```txt
FakerExporterListener=cn.moyada.dubbo.faker.filter.listener.FakerExporterListener
```

   和`META-INF/dubbo/com.alibaba.dubbo.rpc.Filter`文件，
```txt
FakerFilter=cn.moyada.dubbo.faker.filter.filter.FakerFilter
```

  * 确保编译打包项目后class目录下存在对应路径文件


#### 5. 增加dubbo拦截器
```xml
<dubbo:provider filter="FakerFilter" ... />
或
<dubbo:service filter="FakerFilter" ... />

```

#### 6. 配置注解拦截请求信息

使用`Exporter`注解生成调用方法信息，可设置默认参数表达式，使用`Fetch`注解并设置分类拦截请求参数。
观察日志打印`Initializing FakerExporterListener.`和`Initializing FakerFilter.`则表示加入成功。


