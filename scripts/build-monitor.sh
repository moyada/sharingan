#!/usr/bin/env bash

if [[ $(echo $0 | awk '/^\//') == $0 ]]; then
    dir=$(dirname $0)
else
    dir=$PWD/$(dirname $0)
fi

cd $dir
cd ..
base_dir=$(pwd)
mvn clean install -N

cd $base_dir/sharingan-monitor
mvn clean install -Dmaven.test.skip=true

cd $base_dir/sharingan-spring-boot-starter
mvn clean install -Dmaven.test.skip=true