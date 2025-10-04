plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":cookbook-domain"))
    
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk")
}
