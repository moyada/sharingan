#!/usr/bin/env bash

java -server -Xms2048m -Xmx2048m -XX:+UseTLAB -XX:+UseG1GC \
    -XX:+UseCompressedClassPointers -XX:+TieredCompilation -XX:+PerfDisableSharedMem \
    -Djava.net.preferIPv4Stack=true \
    -jar sharingan-manager.jar --spring.config.additional-location=config.properties