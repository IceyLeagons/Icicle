/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

// Fuck IntelliJ...
// Without this we would get an error about implicit usage not being valid here.
// Which... according to gradle documentation is perfectly valid.
// There is a ticket open, that's 8 MONTHS OLD.
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    java
    id("maven-publish")
    id("net.iceyleagons.icicle-gradle") version "1.10.1"
    // alias(libs.plugins.icicle)
}

group = "net.iceyleagons"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    repos.spigot
    repos.jitpack
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.netty)
    compileOnly(libs.fastutil)
    compileOnly(minecraft.spigotApi(libs.versions.spigot.get()))

    implementation(project(":icicle-gui"))
    implementation(project(":icicle-core"))
    implementation(project(":icicle-commands"))
    implementation(project(":icicle-protocol"))
    implementation(project(":icicle-nms"))
    implementation(libs.reflections)
    implementation(project(":icicle-utilities"))

    testImplementation(libs.mockbukkit)
    testImplementation(libs.yaml)

    testCompileOnly(libs.jetbrainsannotations)
    testImplementation(libs.bundles.kotlin)

    testImplementation(libs.bundles.logging)

    testImplementation(libs.bundles.bytebuddy)

    testImplementation(libs.bundles.junit)
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
            artifactId = "icicle-bukkit"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

icicle {
    modifyPluginYml = false

}

tasks.withType<Jar> {
    from(configurations.runtimeClasspath.map { config -> config.map { if (it.isDirectory) it else zipTree(it) } })
}


tasks.test {
    useJUnitPlatform()
}