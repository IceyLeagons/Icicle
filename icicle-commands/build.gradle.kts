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
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    implementation("org.jetbrains:annotations:20.1.0")
    spigotApi(spigotVersion)
    lombok()
}

icicle {
    name = "Icicle-Commands"
    version = project.version.toString()
    description = "A complete command framework ready-to-use in your project!"
    developers = listOf("TOTHTOMI", "Gabe")
}

tasks.test {
    useJUnitPlatform()
}