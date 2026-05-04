ARG BUILD_IMAGE=docker.1ms.run/library/maven:3.9.9-eclipse-temurin-8
ARG RUNTIME_IMAGE=docker.1ms.run/library/eclipse-temurin:8-jre

FROM ${BUILD_IMAGE} AS build
WORKDIR /build

COPY pom.xml /build/pom.xml
COPY weichat-common /build/weichat-common
COPY weichat-api /build/weichat-api

RUN mvn -pl weichat-api -am -DskipTests package

FROM ${RUNTIME_IMAGE}
WORKDIR /app

COPY --from=build /build/weichat-api/target/weichat-api-1.0.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8066
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]
