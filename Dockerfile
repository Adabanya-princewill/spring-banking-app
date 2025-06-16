# Step 1: Use an Ubuntu base image for building
FROM ubuntu:latest AS build

# Install necessary packages
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Step 2: Build the application
RUN mvn clean package -DskipTests

# Step 3: Use a smaller image for running the app
FROM openjdk:17-jre-slim

# Set working directory for the final image
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/springapp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app will run on (default is 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]