plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    shadow(project(":icicle-utilities"))

    implementation("org.reflections:reflections:0.9.12")
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("com.google.guava:guava:31.0.1-jre")
    shadow("org.slf4j:slf4j-api:1.7.32")
    shadow("net.bytebuddy:byte-buddy:1.11.15")
    shadow("net.bytebuddy:byte-buddy-agent:1.11.15")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")


    shadow("ch.qos.logback:logback-core:1.2.9")
    shadow("me.carleslc.Simple-YAML:Simple-Yaml:1.7.2")

    lombok()

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