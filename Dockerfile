# Giai đoạn build
FROM mcr.microsoft.com/playwright/java:v1.52.0-jammy AS build

WORKDIR /home/app

# Copy và build Maven
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn runtime
FROM mcr.microsoft.com/playwright/java:v1.52.0-jammy

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file jar đã build
COPY --from=build /home/app/target/*.jar /app/app.jar

# Thiết lập biến môi trường cho Chromium và debug
ENV PLAYWRIGHT_BROWSERS_PATH=0 \
    DEBUG=pw:api \
    _JAVA_OPTIONS="-Djava.awt.headless=true"

# Mở cổng 8080 (Spring Boot mặc định)
EXPOSE 8080

# Lệnh chạy ứng dụng
CMD ["java", "-jar", "/app/app.jar"]
