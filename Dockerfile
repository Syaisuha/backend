FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . /app
RUN gradle build

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY  /build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]