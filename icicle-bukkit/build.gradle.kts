plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
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
    shadow(project(":icicle-commands"))
    implementation(project(":icicle-utilities"))
    spigotApi(spigotVersion)
    lombok()
}

tasks.test {
    useJUnitPlatform()
}