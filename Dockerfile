# Use the official Maven image as a parent image
FROM maven:3.8.5-openjdk-21 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml to the container
COPY recharge/mvnw .
COPY recharge/pom.xml .

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code to the container
COPY recharge/src ./src

# Package the application
RUN ./mvnw package

# Use the official OpenJDK image for running the application
FROM openjdk:21-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/recharge-0.0.1-SNAPSHOT.jar /app/recharge.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/recharge.jar"]
