import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.vpr42"
version = "1.0.0"

val lombokVersion: String by project
val postgresDriverVersion: String by project


dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Postgres Driver
    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    // Logging
    runtimeOnly("io.github.oshai:kotlin-logging-jvm:7.0.7")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Test
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    enabled = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}
