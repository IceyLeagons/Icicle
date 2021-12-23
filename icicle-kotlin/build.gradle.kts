plugins {
    java
    kotlin("jvm") version "1.6.0"
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

group = "net.iceyleagons"
version = "0.1-SNAPSHOT"

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

    dependencyNotation = "net.iceyleagons:icicle-addon-kotlin:$version"
    description = "Contains standard methods/libraries for speeding up Kotlin development."
    version = project.version.toString()
    developers = listOf("Gabe")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}
