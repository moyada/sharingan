#!/usr/bin/env sh

cd ..
mvn install

cd script
sh build-web.sh