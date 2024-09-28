
# Spring Boot Kotlin Application

This is a Spring Boot application written in Kotlin, using Gradle as the build system.

## Prerequisites

Make sure you have the following installed on your system:

- [JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Gradle 8.x](https://gradle.org/install/)
- [Kotlin 2.0+](https://kotlinlang.org/)
- [PostgreSQL](https://www.postgresql.org/)

## Features

- Spring Boot 3.x
- Kotlin
- Gradle build system
- REST APIs
- PostgreSQL integration (or any other DB of your choice)

## Getting Started

### Clone the repository

```bash
git clone <repository_url>
cd <project_directory>
```

### Configure Database

Set your database connection properties in `application.yml` or `application.properties`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: <db_username>
    password: <db_password>
  jpa:
    hibernate:
      ddl-auto: update
```

### Build and Run the Application

You can build and run the application using the following commands:

```bash
./gradlew clean build
./gradlew bootRun
```

The application will be accessible at `http://localhost:8080`.

## Running Tests

To run the unit tests, use:

```bash
./gradlew test
```

## Building a Docker Image

If you want to build a Docker image for the application, use the following commands:

```bash
./gradlew jibDockerBuild
```

The image will be available locally and can be run with:

```bash
docker run -p 8080:8080 <image_name>
```

## Additional Information

- **Neovim Config:** I use Neovim as my text editor. You can find my configuration [here](https://github.com/vishwesh5544/neovish).
- **Operating System:** I'm using Linux Pop!_OS for all my development work.

## Feedback

Please feel free to provide feedback or suggestions by opening an issue in the relevant assignment repository.

## Contact

If you have any questions or feedback, feel free to [Email Me](mailto:vishweshshukla20@gmail.com). 


