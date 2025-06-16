# Step 1: Use an OpenJDK base image to run the app
FROM openjdk:17-jdk-slim AS build

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the pom.xml and download the Maven dependencies
COPY pom.xml .

# Install Maven to the container
RUN apt-get update && apt-get install -y maven

# Step 4: Build the application (this will create the JAR file)
COPY src ./src
RUN mvn clean package -DskipTests

# Step 5: Use another image to run the app (reduce the image size)
FROM openjdk:17-jre-slim

# Set working directory for the final image
WORKDIR /app

# Step 6: Copy the JAR from the build stage
COPY --from=build /app/target/springapp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app will run on (default is 8080)
EXPOSE 8080

# Step 7: Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
