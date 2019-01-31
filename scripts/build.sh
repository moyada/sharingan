#!/usr/bin/env bash

cd ..
mvn install

cd script
sh build-web.sh