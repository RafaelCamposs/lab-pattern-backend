
plugins {
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.spring") version "2.2.10"
    kotlin("plugin.jpa") version "2.2.10"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    kotlin{
        jvmToolchain(22)
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
    }

}

project(":domain") {
    dependencies {
        implementation("jakarta.inject:jakarta.inject-api:2.0.1")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testImplementation("io.mockk:mockk:1.13.5")
        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project(":application") {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

    dependencies {
        implementation(project(":domain"))
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
        implementation("org.springframework.security:spring-security-crypto")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.flywaydb:flyway-mysql")
        implementation("com.mysql:mysql-connector-j:8.0.33")
        implementation("com.openai:openai-java:1.3.0")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
        runtimeOnly("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testImplementation("org.mockito:mockito-core:4.8.0")
        testImplementation("org.mockito:mockito-junit-jupiter:4.8.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}