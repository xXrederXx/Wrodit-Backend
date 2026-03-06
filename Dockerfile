FROM eclipse-temurin:21
COPY build/libs/Wrodit-Backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV SERVER_ADDRESS=0.0.0.0
ENTRYPOINT ["java", "-jar", "app.jar"]