# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage - dùng Ubuntu và cài Playwright
FROM maven:3.9.6-eclipse-temurin-21

# Cài Chromium và Playwright
RUN apt-get update && apt-get install -y curl wget gnupg ca-certificates fonts-liberation libappindicator3-1 libasound2 libatk-bridge2.0-0 libnspr4 libnss3 libxss1 libxtst6 lsb-release xdg-utils && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get update && apt-get install -y nodejs && \
    npm i -g playwright && \
    playwright install --with-deps

WORKDIR /app
COPY --from=build /home/app/target/*.jar /app/app.jar

EXPOSE 8080
CMD ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT --server.address=0.0.0.0"]
