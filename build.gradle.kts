
plugins {
    id("org.springframework.boot") version "3.5.4" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.spring") version "2.2.10" apply false
    kotlin("plugin.jpa") version "2.2.10" apply false
    id("org.sonarqube") version "7.2.2.6593"
    id("jacoco")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

sonar {
    properties {
        property("sonar.projectKey", "RafaelCamposs_lab-pattern-backend")
        property("sonar.organization", "rafaelcamposs")
        property("sonar.coverage.jacoco.xmlReportPaths", "application/build/reports/jacoco/test/jacocoTestReport.xml,domain/build/reports/jacoco/test/jacocoTestReport.xml")
    }
}

subprojects {
    apply(plugin = "jacoco")

    tasks.withType<Test> {
        finalizedBy(tasks.withType<JacocoReport>())
    }

    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
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
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
        implementation("org.springframework.security:spring-security-crypto")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.flywaydb:flyway-mysql")
        implementation("com.mysql:mysql-connector-j:8.0.33")
        implementation("com.openai:openai-java:4.0.0")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
        runtimeOnly("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.mockk:mockk:1.13.5")
        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        testImplementation("org.springframework.security:spring-security-test")
        testImplementation(platform("org.testcontainers:testcontainers-bom:2.0.3"))
        testImplementation("org.testcontainers:testcontainers")
        testImplementation("org.testcontainers:mysql")
        testImplementation("org.testcontainers:junit-jupiter")
        testRuntimeOnly("com.h2database:h2")

        implementation("io.jsonwebtoken:jjwt-api:0.11.5")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}