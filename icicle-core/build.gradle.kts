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
    alias(libs.plugins.icicle)
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
    repos.jitpack
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    shadow(project(":icicle-utilities"))
    annotationProcessor(project(":icicle-utilities"))

    implementation(libs.reflections)

    // FIXME: Try to get rid of this one!
    compileOnly(libs.guava)

    compileOnly(libs.fastutil)
    compileOnly(libs.bundles.logging)
    compileOnly(libs.bundles.bytebuddy)
    compileOnly(libs.bundles.kotlin)
    compileOnly(libs.jetbrainsannotations)
    compileOnly(libs.yaml)

    testCompileOnly(libs.jetbrainsannotations)
    testImplementation(libs.fastutil)
    testImplementation(libs.bundles.kotlin)
    testImplementation(libs.bundles.bytebuddy)
    testImplementation(libs.yaml)
    testImplementation(libs.bundles.logging)
    testImplementation(libs.guava)
    testImplementation(libs.reflections)
    testImplementation(libs.bundles.junit)
}

icicle {
    name = "Core"
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