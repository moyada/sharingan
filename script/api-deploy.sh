#!/usr/bin/env sh

# 打包上传拦截器
cd ../dubbo-faker-api
JAVA_HOME=$JAVA_7_HOME mvn clean package deploy -Dmaven.test.skip=true