// Fuck IntelliJ...
// Without this we would get an error about implicit usage not being valid here.
// Which... according to gradle documentation is perfectly valid.
// There is a ticket open, that's 8 MONTHS OLD.
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    java
    id("maven-publish")
    alias(libs.plugins.icicle)
}

group = "net.iceyleagons"
version = "1.0.0-SNAPSHOT"

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

publishing {
    repositories {
        maven {
            credentials {
                username = project.properties["ilSnapshotUser"].toString()
                password = project.properties["ilSnapshotPwd"].toString()
            }
            url = uri("https://mvn.iceyleagons.net/snapshots")
        }
    }
    publications {
        create<MavenPublication>("Maven") {
            groupId = "net.iceyleagons"
            artifactId = "icicle-gui"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

icicle {
    name = "Minecraft GUI"
}

tasks.test {
    useJUnitPlatform()
}