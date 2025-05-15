FROM gradle:8.7-jdk21 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Show permissions + contents
RUN chmod +x gradlew && ls -l gradlew && ./gradlew --version

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY  /build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]