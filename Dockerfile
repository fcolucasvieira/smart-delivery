# Build application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencys
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and compile
COPY src ./src
RUN mvn clean package -DskipTests

# Final image to run
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy of the .jar file generated in the build phase
COPY --from=build /app/target/*.jar app.jar

# Exposes the application port
EXPOSE 8080

# Initialization port
ENTRYPOINT ["java", "-jar", "app.jar"]