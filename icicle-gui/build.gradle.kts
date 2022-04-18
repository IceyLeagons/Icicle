plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

group = "net.iceyleagons"
version = "0.1-SNAPSHOT"

val spigotVersion = "1.18.1"

repositories {
    mavenCentral()
    spigot()
    jitpack()
}

dependencies {
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    compileOnly("org.jetbrains:annotations:20.1.0")
    spigotApi(spigotVersion)
    lombok()
}

icicle {
    name = "Minecraft GUI"

    dependencyNotation = "net.iceyleagons:icicle-addon-gui:$version"
    version = project.version.toString()
    description = "A complete GUI library just for you!"
    developers = listOf("TOTHTOMI", "Gabe")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}

tasks.test {
    useJUnitPlatform()
}