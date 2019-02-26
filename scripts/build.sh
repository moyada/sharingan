#!/usr/bin/env bash

DIR=$(cd `dirname $0`;pwd)
cd DIR/..
mvn install

cd script
sh build-web.sh