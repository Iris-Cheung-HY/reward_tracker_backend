# Build jar
FROM eclipse-temurin:25-jdk-alpine AS builder 
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests


#Use jar to run service
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
