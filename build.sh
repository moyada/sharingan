#!/usr/bin/env sh

HOME=$(pwd)

# 编译调用核心
cd dubbo-faker-core
mvn clean install -Dmaven.test.skip=true

cd $HOME

sh build-web.sh
# 编译页面
#cd dubbo-faker-web

#cd dubbo-faker-web/src/page
#npm run build


# 打包项目
#mvn clean package -Dmaven.test.skip=true
