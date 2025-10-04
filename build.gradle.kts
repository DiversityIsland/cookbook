import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

val kotlinVersion: String by project
val springBootVersion: String by project
val springDependencyManagementVersion: String by project
val postgresVersion: String by project
val flywayVersion: String by project
val hibernateVersion: String by project
val junitVersion: String by project
val mockkVersion: String by project
val springMockkVersion: String by project
val jacksonVersion: String by project
val testcontainersVersion: String by project

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
}

allprojects {
    group = "ru.cookbook"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion") {
                bomProperty("kotlin.version", kotlinVersion)
            }
        }
        dependencies {
            // Kotlin
            dependency("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
            dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

            // Database
            dependency("org.postgresql:postgresql:$postgresVersion")
            dependency("org.flywaydb:flyway-core:$flywayVersion")
            dependency("org.flywaydb:flyway-database-postgresql:$flywayVersion")
            
            // Jackson
            dependency("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")

            // Testing
            dependency("org.junit.jupiter:junit-jupiter:$junitVersion")
            dependency("io.mockk:mockk:$mockkVersion")
            dependency("org.testcontainers:postgresql:$testcontainersVersion")
            dependency("org.testcontainers:junit-jupiter:$testcontainersVersion")
        }
    }
}

tasks.register("jar") {
    enabled = false
}
