plugins {
    java
    kotlin("jvm") version "1.6.0"
    id("net.iceyleagons.icicle-gradle") version "1.4-SNAPSHOT"
}

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    implementation(kotlin("stdlib"))
}

icicle {
    name = "Minecraft Protocol"

    dependencyNotation = "net.iceyleagons:icicle-addon-protocol:$version"
    description =
        "Contains standard protocol libraries for use with Bukkit/Spigot servers, where base functions are just not enough."
    version = project.version.toString()
    developers = listOf("Gábe")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}
