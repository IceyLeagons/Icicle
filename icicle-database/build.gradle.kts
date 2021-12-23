plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    implementation(project(":icicle-utilities"))
    implementation(project(":icicle-core"))
    implementation(project(":icicle-serialization"))

    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.jetbrains:annotations:20.1.0")

    // File handling APIs
    shadow("org.json:json:20210307")
    shadow("com.github.Querz:NBT:6.1")

    lombok()

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

icicle {
    name = "Database"
    version = project.version.toString()
    description = "Plenty of serialization options for all your saving/reading needs."
    developers = listOf("TOTHTOMI", "Gabe")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-serialization:0.1-SNAPSHOT"
}

tasks.test {
    useJUnitPlatform()
}