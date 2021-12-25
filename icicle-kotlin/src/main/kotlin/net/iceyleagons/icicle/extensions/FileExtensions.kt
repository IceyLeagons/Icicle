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

fun File.asAdvancedFile(): AdvancedFile = AdvancedFile(this, isDirectory)

fun File.createIfDoesNotExist(ignoreErrors: Boolean = true) =
    if (isDirectory) FileUtils.createFolderIfNotExists(this, ignoreErrors)
    else FileUtils.createFileIfNotExists(this, ignoreErrors)

fun File.isZipped(): Boolean =
    FileZipper.isZipped(this)

fun AdvancedFile.isZipped(): Boolean =
    FileZipper.isZipped(file)

fun File.compress(output: File = File(parent, "${nameWithoutExtension}_compressed.$extension")): File {
    FileZipper.compress(this, output)
    return output
}

fun AdvancedFile.compress(
    output: File = File(
        file.parent,
        "${file.nameWithoutExtension}_compressed.${file.extension}"
    )
): File {
    FileZipper.compress(file, output)
    return output
}

fun File.decompress(output: File = File(parent, "${nameWithoutExtension}_decompressed.$extension")): File {
    FileZipper.decompress(this, output)
    return output
}

fun AdvancedFile.decompress(
    output: File = File(
        file.parent,
        "${file.nameWithoutExtension}_decompressed.${file.extension}"
    )
): File {
    FileZipper.decompress(file, output)
    return output
}

operator fun AdvancedFile.get(file: String): AdvancedFile = getChild(file).asAdvancedFile()

operator fun File.get(file: String): File = File(this, file)

operator fun File.invoke(): File {
    createNewFile()
    return this
}

operator fun AdvancedFile.plusAssign(content: String) = appendToFile(content)

operator fun AdvancedFile.contains(file: String): Boolean = getChild(file).exists()

operator fun File.contains(file: String): Boolean = this[file].exists()

operator fun AdvancedFile.contains(file: AdvancedFile): Boolean =
    if (this.file.parentFile[file.file.name] == file.file) file.file.exists() else false

operator fun AdvancedFile.contains(file: File): Boolean =
    if (this.file.parentFile[file.name] == file) file.exists() else false

infix fun File.sameAs(file: File): Boolean =
    Files.mismatch(toPath(), file.toPath()) == -1L

infix fun AdvancedFile.sameAs(otherFile: AdvancedFile): Boolean =
    Files.mismatch(file.toPath(), otherFile.file.toPath()) == -1L

infix fun File.same(file: File): Boolean =
    this sameAs file

infix fun AdvancedFile.same(otherFile: AdvancedFile): Boolean =
    this sameAs otherFile