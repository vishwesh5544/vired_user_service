# Step 1: Build stage
FROM openjdk:17-jdk-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy only the Gradle wrapper and build scripts first to cache dependencies
COPY gradlew gradlew.bat /app/
COPY gradle/wrapper/ /app/gradle/wrapper/
COPY build.gradle.kts settings.gradle.kts /app/

# Give execution permissions to the Gradle wrapper
RUN chmod +x ./gradlew

# Pre-download dependencies (this will only run if build.gradle.kts or settings.gradle.kts changes)
RUN ./gradlew --no-daemon build || return 0

# Now copy the rest of the application
COPY . .

# Build the project (this will create the WAR file in the build/libs directory)
RUN ./gradlew clean build --no-daemon

# Step 2: Run stage
FROM openjdk:17-jdk-alpine

# Set the working directory for the run stage
WORKDIR /app

# Copy the WAR file from the build stage
COPY --from=build /app/build/libs/*.war /app/user_service.war

# Expose port 8080 (the default Spring Boot port)
EXPOSE 8080

# Set the entry point to run the WAR file
ENTRYPOINT ["java", "-jar", "user_service.war"]


#####################################
## Step 1: Build stage
#FROM openjdk:17-jdk-alpine AS build
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the Gradle wrapper and build scripts
#COPY gradlew gradlew.bat /app/
#COPY gradle/wrapper/ /app/gradle/wrapper/
#COPY build.gradle.kts settings.gradle.kts /app/
#
## Give execution permissions to the Gradle wrapper
#RUN chmod +x ./gradlew
#
## Now copy the rest of the application
#COPY . .
#
## Build the project (this will create the JAR file in the build/libs directory)
#RUN ./gradlew clean build --no-daemon
#
## Step 2: Run stage
#FROM openjdk:17-jdk-alpine
#
## Set the working directory for the run stage
#WORKDIR /app
#
## Copy the JAR file from the build stage
#COPY --from=build /app/build/libs/*.war /app/user_service.war
#
## Expose port 8080 (the default Spring Boot port)
#EXPOSE 8080
#
## Set the entry point to run the JAR file
#ENTRYPOINT ["java", "-jar", "user_service.war"]
