# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage sử dụng Playwright image
FROM mcr.microsoft.com/playwright/java:v1.40.0-jammy AS runtime
WORKDIR /app

# Install Java 21
RUN apt-get update && apt-get install -y openjdk-21-jre

COPY --from=build /home/app/target/ThoiKhoaBieuVnua-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT --server.address=0.0.0.0"]