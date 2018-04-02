#!/usr/bin/env sh


cd ..
HOME=$(pwd)
echo $HOME
# 替换表达式依赖

cd dubbo-faker-core
mvn replace:tag

cd $HOME

cd dubbo-faker-web
mvn replace:tag