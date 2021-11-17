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
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    implementation("org.jetbrains:annotations:20.1.0")
    spigotApi(spigotVersion)
    lombok()
}

icicle {
    name = "Minecraft Commands"

    dependencyNotation = "net.iceyleagons:icicle-addon-commands:$version"
    version = project.version.toString()
    description = "A complete command framework ready-to-use in your project!"
    developers = listOf("TOTHTOMI", "Gabe")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}

tasks.test {
    useJUnitPlatform()
}