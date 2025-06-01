# Build stage
FROM mcr.microsoft.com/playwright/java:v1.44.0-jammy AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM mcr.microsoft.com/playwright/java:v1.44.0-jammy

WORKDIR /app
COPY --from=build /home/app/target/*.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
