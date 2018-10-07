#!/usr/bin/env sh

cd ../sharingan-manager
mvn clean package -Dmaven.test.skip=true

cd ../deploy
sh run.sh