plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.0-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")

    implementation("org.slf4j:slf4j-api:1.7.32")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    testCompileOnly("org.projectlombok:lombok:1.18.20")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.20")

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