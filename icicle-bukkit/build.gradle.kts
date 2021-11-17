plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.4-SNAPSHOT"
}

group = "net.iceyleagons"
version = "0.1-SNAPSHOT"

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
    name = "Bukkit Commons"

    dependencyNotation = "net.iceyleagons:icicle-addon-bukkit:$version"
    version = project.version.toString()
    description = "Implementation of the Icicle ecosystem for Bukkit (or Spigot)."
    developers = listOf("TOTHTOMI", "Gabe")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
}

tasks.test {
    useJUnitPlatform()
}