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

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    spigot()
    jitpack()
}

dependencies {
    lombok()
    // https://mvnrepository.com/artifact/io.netty/netty-all
    compileOnly(libs.netty)
    compileOnly(libs.bytebuddy)
    compileOnly(libs.fastutil)

    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))

    spigotApi(libs.versions.spigot.get())
}

icicle {
    name = "Minecraft NMS"

    dependencyNotation = "net.iceyleagons:icicle-addon-nms:$version"
    description =
        "Contains wrapped representations of NMS classes."
    version = project.version.toString()
    developers = listOf("TOTHTOMI")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}
