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

plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    iglooSnapshots()
    jitpack()
}

dependencies {
    compileOnly(project(":icicle-utilities"))
    compileOnly(project(":icicle-core"))


    compileOnly("org.slf4j:slf4j-api:${findProperty("slf4jApiVersion")}")
    compileOnly("org.jetbrains:annotations:${findProperty("jetbrainsAnnotationVersion")}")

    shadow("net.iceyleagons:nbtlib:${findProperty("nbtLibVersion")}")
    shadow("org.json:json:${findProperty("jsonVersion")}")

    compileOnly("me.carleslc.Simple-YAML:Simple-Yaml:${findProperty("simpleYamlVersion")}")
    compileOnly("org.mongodb:bson:${findProperty("bsonVersion")}")

    lombok()


    testImplementation("org.jetbrains.kotlin:kotlin-reflect:${findProperty("kotlinReflectVersion")}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${findProperty("kotlinStdlibVersion")}")
    testImplementation("com.google.guava:guava:${findProperty("guavaVersion")}")

    testImplementation(project(":icicle-utilities"))
    testImplementation(project(":icicle-core"))

    testImplementation("net.bytebuddy:byte-buddy:${findProperty("byteBuddyVersion")}")
    testImplementation("net.bytebuddy:byte-buddy-agent:${findProperty("byteBuddyAgentVersion")}")
    testImplementation("org.mongodb:bson:${findProperty("bsonVersion")}")
    testImplementation("org.json:json:${findProperty("jsonVersion")}")
    testImplementation("me.carleslc.Simple-YAML:Simple-Yaml:${findProperty("simpleYamlVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${findProperty("jupiterApiVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${findProperty("jupiterEngineVersion")}")
}

icicle {
    name = "Serialization"
    version = project.version.toString()
    description = "Plenty of serialization options for all your saving/reading needs."
    developers = listOf("TOTHTOMI")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}

tasks.test {
    useJUnitPlatform()
}