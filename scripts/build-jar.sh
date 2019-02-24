#!/usr/bin/env bash

DIR=$(cd `dirname $0`;pwd)
cd $DIR/../sharingan-manager
mvn clean package -Dmaven.test.skip=true