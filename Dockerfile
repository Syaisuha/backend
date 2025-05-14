FROM gradle:8.7-jdk21 AS build
WORKDIR /build
COPY . /build
RUN gradle build

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /build

COPY  /build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]