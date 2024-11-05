# Build stage: sử dụng Maven với JDK 23 để build dự án
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage: sử dụng JDK 23 để chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/lyn-0.0.1-SNAPSHOT.war lyn.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "lyn.war"]
