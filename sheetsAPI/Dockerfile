# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first for caching
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build without tests
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app listens on
EXPOSE 10000

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
