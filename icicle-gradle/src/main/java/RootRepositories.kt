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

import net.iceyleagons.gradle.IcicleRepositoryHelper

/**
 * The repository of IceyLeagons' RELEASED libraries.
 */
fun igloo() =
    IcicleRepositoryHelper.igloo()

/**
 * The repository of IceyLeagons' WIP or UNSTABLE libraries/library versions.
 */
fun iglooSnapshots() =
    IcicleRepositoryHelper.iglooSnapshots()

/**
 * The repository of Sonatype.
 */
fun sonatype() =
    IcicleRepositoryHelper.sonatype()

/**
 * Repository containing NMS dependencies.
 */
fun codemc() =
    IcicleRepositoryHelper.codemc()

/**
 * The repository that's responsible for the Spigot libraries.
 */
fun spigot() =
    IcicleRepositoryHelper.spigot()

/**
 * The repository that's responsible for the Paper libraries.
 */
fun paper() =
    IcicleRepositoryHelper.paper()

/**
 * Repository of libraries compiled from common Git sources. Needed for lots of dependencies.
 */
fun jitpack() =
    IcicleRepositoryHelper.jitpack()