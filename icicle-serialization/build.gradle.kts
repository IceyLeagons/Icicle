plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.4-SNAPSHOT"
}

version = "1.0.0"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    implementation(project(":icicle-utilities"))
    implementation(project(":icicle-core"))

    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.jetbrains:annotations:20.1.0")

    // File handling APIs (TODO some of these may already be shadowed in core, we don't want duplicates -> fix)
    shadow("org.json:json:20210307")
    shadow("com.github.Querz:NBT:6.1")
    shadow("me.carleslc.Simple-YAML:Simple-Yaml:1.7.2")

    lombok()

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

icicle {
    name = "Serialization"
    version = project.version.toString()
    description = "Plenty of serialization options for all your saving/reading needs."
    developers = listOf("TOTHTOMI")
}

tasks.test {
    useJUnitPlatform()
}