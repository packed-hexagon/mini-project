# Use a valid OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the default environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
