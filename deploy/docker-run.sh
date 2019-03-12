#!/usr/bin/env bash

DIR=$HOME/Volums/sharingan

docker run -itd --rm \
    --name sharingan \
    --cpu-period=100000 --cpu-quota=20000 \
    -m 300M --memory-reservation 100M \
    -v $DIR:/logs \
    -p 8080:8080 \
    --network=bridge-test \
    sharingan:lasted


docker run -itd --rm \
    --name sharingan \
    --cpu-period=100000 --cpu-quota=20000 \
    -m 300M --memory-reservation 100M \
    -p 8080:8080 \
    sharingan:lasted


docker run --rm --name sharingan -p 8080:8080 sharingan:lasted