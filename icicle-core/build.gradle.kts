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

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    lombok()
    shadow(project(":icicle-utilities"))
    annotationProcessor(project(":icicle-utilities"))

    implementation("org.reflections:reflections:${findProperty("reflectionsVersion")}")
    compileOnly("com.google.guava:guava:${findProperty("guavaVersion")}")
    compileOnly("org.slf4j:slf4j-api:${findProperty("slf4jApiVersion")}")

    compileOnly("net.bytebuddy:byte-buddy:${findProperty("byteBuddyVersion")}")
    compileOnly("net.bytebuddy:byte-buddy-agent:${findProperty("byteBuddyAgentVersion")}")

    compileOnly("org.jetbrains.kotlin:kotlin-reflect:${findProperty("kotlinReflectVersion")}")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:${findProperty("kotlinStdlibVersion")}")

    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    compileOnly("org.jetbrains:annotations:${findProperty("jetbrainsAnnotationVersion")}")


    compileOnly("ch.qos.logback:logback-core:${findProperty("logbackVersion")}")
    compileOnly("me.carleslc.Simple-YAML:Simple-Yaml:${findProperty("simpleYamlVersion")}")

    testImplementation("me.carleslc.Simple-YAML:Simple-Yaml:${findProperty("simpleYamlVersion")}")

    testCompileOnly("org.jetbrains:annotations:${findProperty("jetbrainsAnnotationVersion")}")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:${findProperty("kotlinReflectVersion")}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${findProperty("kotlinStdlibVersion")}")

    testImplementation("org.slf4j:slf4j-api:${findProperty("slf4jApiVersion")}")
    testImplementation("ch.qos.logback:logback-core:${findProperty("logbackVersion")}")

    testImplementation("net.bytebuddy:byte-buddy:${findProperty("byteBuddyVersion")}")
    testImplementation("net.bytebuddy:byte-buddy-agent:${findProperty("byteBuddyAgentVersion")}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${findProperty("jupiterApiVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${findProperty("jupiterEngineVersion")}")
}

icicle {
    name = "Core"
    version = project.version.toString()
    description = "The main driving force/power house of the icicle ecosystem."
    developers = listOf("TOTHTOMI", "Gabe")
}

tasks.withType<Test> {
    testLogging {
        events(
            org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
        showStandardStreams = true
    }

    useJUnitPlatform()

    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2)
        .takeIf { it > 0 }
        ?: 1
}