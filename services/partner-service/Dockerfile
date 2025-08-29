FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/partner-service-0.0.1-SNAPSHOT.jar partner-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","partner-service.jar"]
