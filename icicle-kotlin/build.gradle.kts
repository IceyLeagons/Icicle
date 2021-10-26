plugins {
    java
    kotlin("jvm") version "1.5.30"
    id("net.iceyleagons.icicle-gradle") version "1.4-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    shadow(kotlin("stdlib"))
}

icicle {
    name = "Standard Kotlin"
    description = "Contains standard methods/libraries for speeding up Kotlin development."
    version = project.version.toString()
    developers = listOf("Gabe")
}
