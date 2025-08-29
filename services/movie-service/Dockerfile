FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/movie-service-0.0.1-SNAPSHOT.jar movie-service.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","movie-service.jar"]
