plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.0-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(project(":icicle-utilities"))

    implementation("org.reflections:reflections:0.9.12")
    implementation("org.jetbrains:annotations:20.1.0")
    shadow("org.slf4j:slf4j-api:1.7.32")
    shadow("net.bytebuddy:byte-buddy:1.11.15")
    shadow("net.bytebuddy:byte-buddy-agent:1.11.15")

    shadow("org.slf4j:slf4j-log4j12:1.7.32")
    shadow("me.carleslc.Simple-YAML:Simple-Yaml:1.7.2")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    testCompileOnly("org.projectlombok:lombok:1.18.20")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.20")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

icicle {
    name = "Core"
    version = project.version.toString()
    description = "The main driving force/power house of the icicle ecosystem."
    developers = listOf("TOTHTOMI", "Gabe")
}

tasks.test {
    useJUnitPlatform()
}