// Fuck IntelliJ...
// Without this we would get an error about implicit usage not being valid here.
// Which... according to gradle documentation is perfectly valid.
// There is a ticket open, that's 8 MONTHS OLD.
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    java
    alias(libs.plugins.icicle)
}

group = "net.iceyleagons"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    repos.spigot
    repos.jitpack
}

dependencies {
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrainsannotations)
    compileOnly(minecraft.spigotApi(libs.versions.spigot.get()))
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

icicle {
    name = "Minecraft GUI"
}

tasks.test {
    useJUnitPlatform()
}