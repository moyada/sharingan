#!/usr/bin/env sh


# 编译页面
cd ../dubbo-faker-web

# 使用插件替换pom文件依赖
#mvn replace:tag

# 打包项目
mvn clean package -Dmaven.test.skip=true
