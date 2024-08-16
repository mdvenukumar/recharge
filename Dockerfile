# Stage 1: Build the JAR file using Maven
FROM eclipse-temurin:21-jdk-jammy AS builder

# Set the working directory
WORKDIR /opt/app

# Copy the Maven wrapper and configuration files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make Maven wrapper script executable
RUN chmod +x mvnw

# Ensure dependencies are downloaded
RUN ./mvnw dependency:go-offline

# Copy source code
COPY ./src ./src

# Build the application
RUN ./mvnw clean install -DskipTests

# Stage 2: Run the JAR file
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /opt/app

# Expose port 8080
EXPOSE 8080

# Copy the JAR file from the builder stage
COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

# Run the JAR file
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/opt/app/app.jar"]
