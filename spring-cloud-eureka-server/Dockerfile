# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/eurekaserver-0.0.1-SNAPSHOT.jar /app.jar

#Expose
EXPOSE 8761

# Set the command to run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]

RUN chmod go-w /usr/share/filebeat/filebeat.yml


