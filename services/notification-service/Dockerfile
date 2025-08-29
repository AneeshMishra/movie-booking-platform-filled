FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","notification-service.jar"]
