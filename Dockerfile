FROM mcr.microsoft.com/playwright/java:v1.52.0-jammy AS build

WORKDIR /home/app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM mcr.microsoft.com/playwright/java:v1.52.0-jammy

WORKDIR /app

COPY --from=build /home/app/target/*.jar /app/app.jar

ENV PLAYWRIGHT_BROWSERS_PATH=0 \
    DEBUG=pw:api \
    _JAVA_OPTIONS="-Djava.awt.headless=true"

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
