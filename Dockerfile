# -------- BUILD STAGE --------
FROM gradle:8.6-jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

# -------- RUNTIME STAGE --------
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENV SERVER_ADDRESS=0.0.0.0

ENTRYPOINT ["java", "-jar", "app.jar"]