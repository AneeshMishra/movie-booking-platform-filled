# ====== Stage 1: Build ======
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
COPY partner-service/pom.xml partner-service/
COPY movie-service/pom.xml movie-service/
COPY seat-service/pom.xml seat-service/
COPY booking-service/pom.xml booking-service/
COPY payment-service/pom.xml payment-service/
COPY notification-service/pom.xml notification-service/

RUN mvn -B dependency:go-offline

# Copy all source code
COPY . .

# Package the current service (skip tests for faster build)
ARG SERVICE
RUN mvn -pl ${SERVICE} -am clean package -DskipTests

# ====== Stage 2: Runtime ======
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy jar from builder stage
ARG SERVICE
COPY --from=builder /app/${SERVICE}/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
