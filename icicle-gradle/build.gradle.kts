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
    kotlin("jvm") version libs.versions.kotlin.asProvider()
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

val extraLibs = configurations.create("extraLibs")

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    extraLibs("com.amihaiemil.web:eo-yaml:5.2.2")
    configurations.implementation.get().extendsFrom(extraLibs)
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.jar {
    from(extraLibs.map { if (it.isDirectory) it else zipTree(it) })
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.iceyleagons.icicle-gradle"
            artifactId = "net.iceyleagons.icicle-gradle.gradle.plugin"
            version = "1.8.0"

            from(components["java"])
        }
    }

    repositories {
        maven {
            credentials {
                username = property("ilSnapshotUser") as String
                password = property("ilSnapshotPwd") as String
            }

            url = uri("https://mvn.iceyleagons.net/snapshots")
        }
    }
}

gradlePlugin {
    plugins {
        create("net.iceyleagons.icicle-gradle") {
            id = "net.iceyleagons.icicle-gradle"
            implementationClass = "net.iceyleagons.gradle.IciclePlugin"
        }
    }
}