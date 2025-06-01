# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage - Sử dụng Playwright image v1.52.0
FROM mcr.microsoft.com/playwright/java:v1.52.0-jammy AS runtime
WORKDIR /app

COPY --from=build /home/app/target/ThoiKhoaBieuVnua-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT --server.address=0.0.0.0"]