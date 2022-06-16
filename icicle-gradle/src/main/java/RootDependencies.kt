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

import net.iceyleagons.gradle.IcicleDependencyHelper

/**
 * Adds the icicle-core dependency into the project it is called in.
 */
fun icicle() =
    IcicleDependencyHelper.icicleCore()

/**
 * Adds lombok into annotationProcessor and compileOnly.
 */
fun lombok() =
    IcicleDependencyHelper.lombok()

/**
 * Adds the spigot api into the project it is called in as implementation.
 */
fun spigotApi() =
    IcicleDependencyHelper.spigotApi()

/**
 * Adds the spigot api into the project it is called in as implementation with the specified version.
 */
fun spigotApi(version: String) =
    IcicleDependencyHelper.spigotApi(version)

/**
 * Adds the paper api into the project it is called in as implementation.
 */
fun paperApi() =
    IcicleDependencyHelper.paperApi()

/**
 * Adds the paper api into the project it is called in as implementation with the specified version.
 */
fun paperApi(version: String) =
    IcicleDependencyHelper.paperApi(version)

/**
 * Adds the minecraft sources into the project it is called in as implementation.
 */
fun nms() =
    IcicleDependencyHelper.nms()

/**
 * Adds the minecraft sources into the project it is called in as implementation with the specified version.
 */
fun nms(version: String) =
    IcicleDependencyHelper.nms(version)