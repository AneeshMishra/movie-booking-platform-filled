FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/payment-service-0.0.1-SNAPSHOT.jar payment-service.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","payment-service.jar"]
