plugins {
    java
    kotlin("jvm") version "1.5.30"
}

version = "1.0-ALPHA"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Web API
    implementation("io.javalin:javalin:4.1.1")

    // Database Manager
    implementation("org.jetbrains.exposed:exposed-core:0.36.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
    runtimeOnly("org.xerial:sqlite-jdbc:3.36.0.3")

    // Password hashing
    implementation("at.favre.lib:bcrypt:0.9.0")

    // Logging
    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
}
