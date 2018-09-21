### Sharingan

### 背景
在项目快速迭代时期，由于工期紧凑、逻辑复杂、测试范围不全等原因可能疏忽程序中异常情况。

`sharingan`可用于快速检测回归服务`可用性`，通过定义策略对边界值进行检查，保障代码的可靠性。


### 快速开始

1. 项目需要`jdk1.8`以上版本，通过[这里](https://github.com/moyada/sharingan/releases) 下载 sharingan-manager 压缩包进行解压。

2. 初始化mysql数据库，执行`schema`文件下的`info.sql`和`invoke.sql`。

3. 对`conf.properties`文件进行配置:

```
# nexus3仓库地址
maven.host = http://127.0.0.1:8081

# mysql连接信息
spring.datasource.url = jdbc:mysql://127.0.0.1:3306/sharingan?useUnicode=true&amp&characterEncoding=UTF-8&useSSL=false
spring.datasource.username = root
spring.datasource.password = root

# dubbo注册中心
dubbo.registry = zookeeper://127.0.0.1:2181
dubbo.username =
dubbo.password =

```
4. 启动sharingan: `./run.sh`

5. 访问链接 `htto://127.0.0.1:8080/index.html` 进入管理界面。

### 功能

1. 动态进行类加载，通过配置`app_info`的项目仓库坐标，运行期从`nexus`版本仓库中获取最新依赖。

2. 参数表达式，配置`function_info`中`expression`来指定规则动态生成请求参数，表达式格式为`${项目名称.数据领域}`，默认项目名称为`app_info`表中的`name`、数据领域为`invoke_param`中的`domain`。

3. rpc协议支持，目前支持[dubbo](https://github.com/apache/incubator-dubbo)，计划加入协议`Spring Cloud`、`Sofa`、`Istio`。

4. 监听项目接口调用，将请求的数据保存用于参数表达式使用。(开发中)