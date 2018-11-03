使用指南
=============

## 配置

1. 项目需要 `jdk1.8` 以上版本，通过[这里](https://github.com/moyada/sharingan/releases)下载 sharingan-manager 压缩包进行解压。

2. 初始化mysql数据库，执行 `schema` 文件下的 `info.sql` 和 `invoke.sql` 。

3. 对 `conf.properties` 文件进行配置:

| 参数 | 描述 |
| --- | ---- |
| spring.datasource.url | mysql连接地址 |
| spring.datasource.username | mysql连接用户名 |
| spring.datasource.password | mysql连接密码 |
| maven.host | nexus3仓库地址(可选) |
| maven.username | nexus3仓库账号(可选) |
| maven.username | nexus3仓库密码(可选) |
| dubbo.registry | dubbo注册中心地址(可选) |
| dubbo.username | dubbo注册中心账号(可选) |
| dubbo.password | dubbo注册中心密码(可选) |
| eureka.client.serviceUrl.defaultZone | SpringCloud-eureka注册地址(可选) |

4. 启动sharingan: `./run.sh`

5. 访问链接 `htto://127.0.0.1:8080/index.html` 进入管理界面。

## 说明

* 动态进行类加载: 在非http调用时，通过 `MetadataRepository` 查询的 `AppDO` 获取项目的仓库坐标，从版本仓库中获取最新依赖，
同时时卸载过期类加载器。

* 领域数据表达式: `MetadataRepository` 查询的 `FunctionDO` 中 `expression` 字段来指定规则动态生成请求参数，表达式格式为 `${项目名称.数据领域}` 。
  默认mysql实现中，项目名称为 `app_info` 表中的 `name` 、数据领域为 `invoke_param` 中的 `domain` 。

* 数值表达式: 默认带有整数和浮点数的生成表达式，
整数表达式: 可设置随机范围 `#{int.开始-结束}` ，或者无范围 `#{int.random}` 。
浮点数表达式: 可设置随机范围 `#{double[精度].开始-结束}` ，或者无范围 `#{double.random}` ，对于 `[精度]` 设置可选，缺省为3。

## 示例
测试数据位于 `schema` 文件下的 `test.sql` 。

### 常量
![example_1](images/example_1.png)

### 领域表达式
![example_2](images/example_2.png)

### 数值表达式
![example_4](images/example_3.png)

### 复合表达式
![example_3](images/example_4.png)