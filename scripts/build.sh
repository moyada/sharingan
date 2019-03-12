#!/usr/bin/env bash

DIR=$(cd `dirname $0`;pwd)
cd $DIR/..
mvn install -Dmaven.test.skip=true

sh scripts/build-deploy.sh