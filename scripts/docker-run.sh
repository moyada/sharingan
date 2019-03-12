#!/usr/bin/env bash

DIR=/Users/xueyikang/Volums/sharingan

mkdir $DIR && chown -R 200 $DIR

docker run -itd --rm \
    --name sharingan-container \
    -v $DIR:/logs –privileged=true \
    --cpu-period=100000 --cpu-quota=20000 \
    -m 300M --memory-reservation 100M \
    -p 8080:8080 \
    --restart=on-failure:10 \
    sharingan:lasted

docker run -itd --rm \
    --name sharingan \
    -p 8080:8080 \
    --network=bridge-test \
    sharingan:lasted

    --cpu-period=100000 --cpu-quota=20000 \
    -m 300M --memory-reservation 100M \
    -v /Users/xueyikang/Volums/sharingan:/logs \

mkdir /Users/xueyikang/Volums/sharingan && chown -R 200 /Users/xueyikang/Volums/sharingan


mkdir /Users/xueyikang/nexus-data && chown -R 200 /Users/xueyikang/nexus-data

docker run -d --rm -p 8081:8081 --name nexus -v /Users/xueyikang/nexus-data:/nexus-data sonatype/nexus3