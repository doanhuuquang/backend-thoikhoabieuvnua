ENV MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/?retryWrites=true&w=majority
ENV MONGODB_DATABASE=thoikhoabieuvnua

# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Install Node.js and npm (cần để chạy Playwright CLI)
RUN apt-get update && apt-get install -y \
    curl \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs

# Cài đặt Playwright và dependencies
RUN npm install -g playwright@latest \
    && playwright install-deps

COPY --from=build /home/app/target/ThoiKhoaBieuVnua-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT --server.address=0.0.0.0"]