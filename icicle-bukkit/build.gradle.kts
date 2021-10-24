plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.0-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

val spigotVersion = "1.17.1-R0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    shadow(project(":icicle-core"))
    implementation("org.spigotmc:spigot-api:${spigotVersion}")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    testCompileOnly("org.projectlombok:lombok:1.18.20")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.20")
}

icicle {
    name = "Icicle-Bukkit"
    version = project.version.toString()
    description = "Implementation of the Icicle ecosystem for Bukkit (or Spigot)."
    developers = listOf("TOTHTOMI", "Gabe")
}

tasks.test {
    useJUnitPlatform()
}