# Dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Установим curl для health check
RUN apt-get update && apt-get install -y curl

EXPOSE 8080

# Добавим задержку для полной инициализации БД
ENTRYPOINT ["sh", "-c", "sleep 10 && java -jar app.jar"]
