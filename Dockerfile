FROM java:openjdk-8-jre-alpine AS builder

COPY deploy/sharingan-manager.jar .
COPY deploy/config.properties .
COPY deploy/run.sh .

VOLUME ["/logs"]

EXPOSE 18000

ENTRYPOINT ["mount("","/",NULL,MS_PRIVATE,"")"]

ENTRYPOINT [ "sh", "run.sh" ]