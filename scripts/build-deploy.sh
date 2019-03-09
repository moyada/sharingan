#!/usr/bin/env bash

DIR=$(cd `dirname $0`;pwd)
cd $DIR/../sharingan-web
mvn clean package -Dmaven.test.skip=true

cd $DIR/../deploy

tar -czvf $DIR/../sharingan-manager.tar.gz .