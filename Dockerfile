# FROM gradle:8.7-jdk21 AS build
# WORKDIR /app

# COPY gradle gradle
# COPY gradlew .
# COPY build.gradle .
# COPY settings.gradle .
# COPY src src

# # Show permissions + contents
# RUN chmod +x gradlew && ls -l gradlew && ./gradlew --version

# WORKDIR /app/build
# RUN mkdir libs
# WORKDIR /app

# RUN ./gradlew bootJar --no-daemon

# FROM eclipse-temurin:21-jdk-jammy

# WORKDIR /app

# COPY /build/libs/*.jar app.jar

# EXPOSE 8080

# ENTRYPOINT ["java","-jar","app.jar"]

# Stage 1: Build the jar using Gradle
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

# Copy only necessary files to cache dependencies better
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle --no-daemon build -x test || true

# Copy the rest of the source code
COPY src ./src

# Build the project (skip tests to speed up)
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the app
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
