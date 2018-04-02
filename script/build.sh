#!/usr/bin/env sh

cd ..
HOME=$(pwd)

# 编译调用核心
cd dubbo-faker-core
mvn clean install -Dmaven.test.skip=true

cd $HOME/script

sh build-web.sh

# 编译页面
#sh build-page.sh
#cd $HOME

# 打包项目
#mvn clean package -Dmaven.test.skip=true
