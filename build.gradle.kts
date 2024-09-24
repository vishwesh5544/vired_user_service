plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    war
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.devops7"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

//tasks.named<War>("war") {
//    archiveFileName.set("user_service.war")  // Set the WAR file name explicitly
////    manifest {
////        attributes(
////            "Manifest-Version" to "1.0",
////            "Main-Class" to "com.devops7.user_service.UserServiceApplication",  // Replace with your main class
////            "Start-Class" to "com.devops7.user_service.UserServiceApplication",  // For Spring Boot
////            "Implementation-Title" to "User Service Application",
////            "Implementation-Version" to version
////        )
////    }
//
//    manifest {
//        attributes(
////            "Main-Class" to "org.springframework.boot.loader.WarLauncher",  // Spring Boot WAR launcher
//            "Main-Class" to "com.devops7.user_service.UserServiceApplicationKt",  // Spring Boot WAR launcher
////            "Start-Class" to "com.devops7.user_service.UserServiceApplication"  // Your main application class
//        )
//    }
//}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootWar> {
    mainClass.set("com.devops7.user_service.UserServiceApplicationKt")  // Set the correct main class (replace with yours)
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}
