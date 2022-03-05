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

package net.iceyleagons.icicle.extensions

import net.iceyleagons.icicle.utilities.file.AdvancedFile
import net.iceyleagons.icicle.utilities.file.FileUtils
import net.iceyleagons.icicle.utilities.file.FileZipper
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun File.asAdvancedFile(): AdvancedFile = AdvancedFile(this, isDirectory)
fun Path.asAdvancedFile(): AdvancedFile = AdvancedFile(this, Files.isDirectory(this))

fun File.createIfDoesNotExist(ignoreErrors: Boolean = true) =
    if (isDirectory) FileUtils.createDirectoryIfNotExists(this.toPath(), ignoreErrors)
    else FileUtils.createFileIfNotExists(this.toPath(), ignoreErrors)

fun File.isZipped(): Boolean =
    FileZipper.isZipped(this.toPath())

fun AdvancedFile.isZipped(): Boolean =
    FileZipper.isZipped(this.path)

fun File.compress(output: File = File(parent, "${nameWithoutExtension}_compressed.$extension")): File {
    FileZipper.compress(this.toPath(), output.toPath())
    return output
}

fun AdvancedFile.compress(
    output: AdvancedFile = AdvancedFile(
        path.parent.resolve("${this.filenameWithoutExtension}_compressed.${FileUtils.getExtension(path)}")
    )
): AdvancedFile {
    FileZipper.compress(path, output.path)
    return output
}

fun File.decompress(output: File = File(parent, "${nameWithoutExtension}_decompressed.$extension")): File {
    FileZipper.decompress(this.toPath(), output.toPath())
    return output
}

fun AdvancedFile.decompress(
    output: AdvancedFile = AdvancedFile(
        path.parent.resolve("${this.filenameWithoutExtension}_decompressed.${FileUtils.getExtension(path)}")
    )
): AdvancedFile {
    FileZipper.decompress(path, output.path)
    return output
}

operator fun AdvancedFile.get(file: String): AdvancedFile = getChild(file).asAdvancedFile()

operator fun File.get(file: String): File = File(this, file)

operator fun File.invoke(): File {
    createNewFile()
    return this
}

operator fun AdvancedFile.plusAssign(content: String) = appendToFile(content)

operator fun AdvancedFile.contains(file: String): Boolean = Files.exists(getChild(file))

operator fun File.contains(file: String): Boolean = this[file].exists()

operator fun AdvancedFile.contains(file: AdvancedFile): Boolean =
    if (this.path.parent.resolve(file.path.fileName) == file.path) Files.exists(file.path) else false

operator fun AdvancedFile.contains(file: File): Boolean =
    if (this.path.parent.resolve(file.name) == file) file.exists() else false

infix fun File.sameAs(file: File): Boolean =
    Files.mismatch(toPath(), file.toPath()) == -1L

infix fun AdvancedFile.sameAs(otherFile: AdvancedFile): Boolean =
    Files.mismatch(path, otherFile.path) == -1L

infix fun File.same(file: File): Boolean =
    this sameAs file

infix fun AdvancedFile.same(otherFile: AdvancedFile): Boolean =
    this sameAs otherFile