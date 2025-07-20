FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/testovoe_zadanie-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties .
COPY src/main/resources/logback.xml .

RUN mkdir -p /app/output /app/logs

ENV DB_URL=jdbc:postgresql://host.docker.internal:5432/testovoe_zadanie \
    DB_USER=postgres \
    DB_PASSWORD=babaka456

ENTRYPOINT ["java", "-Ddb.url=${DB_URL}", "-Ddb.user=${DB_USER}", "-Ddb.password=${DB_PASSWORD}", "-jar", "app.jar"]