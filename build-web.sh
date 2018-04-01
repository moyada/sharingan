#!/usr/bin/env sh


# 编译页面
cd dubbo-faker-web

# 打包项目
mvn clean package -Dmaven.test.skip=true
