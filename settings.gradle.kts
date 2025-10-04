pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "cookbook"

include(
    "cookbook-api",
    "cookbook-app",
    "cookbook-domain",
    "cookbook-ui"
)
