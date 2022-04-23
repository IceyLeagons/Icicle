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
    jacoco
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

group = "net.iceyleagons"
version = "1.0.0"

repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    shadow(project(":icicle-utilities"))

    implementation("org.reflections:reflections:0.9.12")
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("com.google.guava:guava:31.0.1-jre")
    compileOnly("org.slf4j:slf4j-api:1.7.32")

    compileOnly("net.bytebuddy:byte-buddy:1.11.15")
    compileOnly("net.bytebuddy:byte-buddy-agent:1.11.15")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.31")


    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    compileOnly("org.jetbrains:annotations:23.0.0")


    compileOnly("ch.qos.logback:logback-core:1.2.9")
    compileOnly("me.carleslc.Simple-YAML:Simple-Yaml:1.8")

    lombok()

    testImplementation("me.carleslc.Simple-YAML:Simple-Yaml:1.8")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.20")
    testImplementation("org.slf4j:slf4j-api:1.7.32")
    testImplementation("net.bytebuddy:byte-buddy:1.11.15")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.11.15")
    testCompileOnly("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("ch.qos.logback:logback-core:1.2.9")
}

icicle {
    name = "Core"
    version = project.version.toString()
    description = "The main driving force/power house of the icicle ecosystem."
    developers = listOf("TOTHTOMI", "Gabe")
}

jacoco {
    toolVersion = "0.8.7"
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


tasks.test {
    extensions.configure(JacocoTaskExtension::class) {
        setDestinationFile(file("$buildDir/jacoco/jacoco.exec"))
    }

    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    reports {
        html.required.set(false)
        csv.required.set(false)
        xml.required.set(true)
        xml.outputLocation.set(file("./build/reports/jacoco/icicle-core-report.xml"))
    }

    executionData(fileTree(project.buildDir).include("jacoco/*.exec"))
    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.0".toBigDecimal()
            }
        }
        rule {
            enabled = true
            element = "CLASS"
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
            }
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
            }
            excludes = listOf()
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification"
    )

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}