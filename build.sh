#!/usr/bin/env sh

# 编译页面
#HOME=$(pwd)
#cd dubbo-faker-ui/src/page
#npm run build
#cd $HOME

# 打包项目
mvn clean package -Dmaven.test.skip=true