FROM java:openjdk-8-jre-alpine AS builder

COPY . .

VOLUME ["/logs"]

EXPOSE 30000

ENTRYPOINT [ "mount("","/",NULL,MS_PRIVATE,"")" ]

ENTRYPOINT [ "sh", "run.sh" ]