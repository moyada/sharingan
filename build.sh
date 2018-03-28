#!/usr/bin/env sh

HOME=$(pwd)
cd dubbo-faker-ui/src/page
npm run build
cd $HOME
mvn clean package -Dmaven.test.skip=true