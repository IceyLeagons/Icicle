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
    kotlin("jvm") version "1.5.30"
}

version = "1.0-ALPHA"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Web API
    implementation("io.javalin:javalin:4.1.1")

    // Database Manager
    implementation("org.jetbrains.exposed:exposed-core:0.36.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
    runtimeOnly("org.xerial:sqlite-jdbc:3.36.0.3")

    // Password hashing
    implementation("at.favre.lib:bcrypt:0.9.0")

    // Logging
    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
}
