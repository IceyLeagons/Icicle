plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.4-SNAPSHOT"
}

group = "net.iceyleagons"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("org.slf4j:slf4j-api:1.7.32")
    shadow("com.google.guava:guava:31.0.1-jre")

    lombok()

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

icicle {
    name = "Utilities"
    version = project.version.toString()
    description = "Contains utility classes that may or may not prove to be useful."
    developers = listOf("TOTHTOMI", "Gabe")
}

tasks.test {
    useJUnitPlatform()
}