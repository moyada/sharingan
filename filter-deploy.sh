#!/usr/bin/env sh

# 打包上传拦截器
cd dubbo-faker-filter
mvn clean package deploy -Dmaven.test.skip=true