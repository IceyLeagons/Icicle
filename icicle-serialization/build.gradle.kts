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
    alias(libs.plugins.icicle)
}

version = "1.3.0-SNAPSHOT"

repositories {
    mavenCentral()
    repos.iglooSnapshots
    repos.jitpack
}

dependencies {
    compileOnly(project(":icicle-utilities"))
    compileOnly(project(":icicle-core"))

    compileOnly(libs.slf4j)
    compileOnly(libs.jetbrainsannotations)
    compileOnly(libs.fastutil)
    shadow(libs.nbt)
    shadow(libs.json)
    compileOnly(libs.yaml)
    compileOnly(libs.bson)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.slf4j)
    testImplementation(libs.bundles.kotlin)
    testImplementation(libs.guava)
    testImplementation(libs.fastutil)

    testImplementation(project(":icicle-utilities"))
    testImplementation(project(":icicle-core"))

    testImplementation(libs.bundles.bytebuddy)
    testImplementation(libs.bson)
    testImplementation(libs.json)
    testImplementation(libs.yaml)
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
            artifactId = "icicle-serialization"
            version = "1.3.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

icicle {
    name = "Serialization"
}

tasks.test {
    useJUnitPlatform()
}