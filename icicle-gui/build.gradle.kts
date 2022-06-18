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
    spigot()
    jitpack()
}

dependencies {
    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrainsannotations)
    spigotApi(libs.versions.spigot.get())
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