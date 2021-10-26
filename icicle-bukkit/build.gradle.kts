plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.4-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

val spigotVersion = "1.17.1"

repositories {
    mavenCentral()
    spigot()
    jitpack()
}

dependencies {
    shadow(project(":icicle-core"))
    spigotApi(spigotVersion)
    lombok()
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