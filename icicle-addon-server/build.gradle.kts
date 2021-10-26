plugins {
    java
    kotlin("jvm") version "1.5.30"
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.javalin:javalin:4.1.0")
    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
    testImplementation("junit", "junit", "4.12")
}
