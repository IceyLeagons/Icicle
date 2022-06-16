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

/**
 * Converts a File into an AdvancedFile.
 */
fun File.asAdvancedFile(): AdvancedFile = AdvancedFile(this, isDirectory)

/**
 * Converts a Path into an AdvancedFile.
 */
fun Path.asAdvancedFile(): AdvancedFile = AdvancedFile(this, Files.isDirectory(this))

/**
 * Creates a file if it does not exist.
 */
fun File.createIfDoesNotExist(ignoreErrors: Boolean = true) =
    if (isDirectory) FileUtils.createDirectoryIfNotExists(this.toPath(), ignoreErrors)
    else FileUtils.createFileIfNotExists(this.toPath(), ignoreErrors)

/**
 * @return whether the specified file is a Zip.
 */
fun File.isZipped(): Boolean =
    FileZipper.isZipped(this.toPath())

/**
 * @return whether the specified file is a Zip.
 */
fun AdvancedFile.isZipped(): Boolean =
    FileZipper.isZipped(this.path)

/**
 * Zips a file.
 *
 * @param output the file where we want the compressed archive to be.
 * @return the output file specified.
 */
fun File.compress(output: File = File(parent, "${nameWithoutExtension}_compressed.$extension")): File {
    FileZipper.compress(this.toPath(), output.toPath())
    return output
}

/**
 * Zips a file.
 *
 * @param output the file where we want the compressed archive to be.
 * @return the output file specified.
 */
fun AdvancedFile.compress(
    output: AdvancedFile = AdvancedFile(
        path.parent.resolve("${this.filenameWithoutExtension}_compressed.${FileUtils.getExtension(path)}")
    )
): AdvancedFile {
    FileZipper.compress(path, output.path)
    return output
}

/**
 * Decompresses / Unzips a file.
 *
 * @param output the file where we want the uncompressed archive to be.
 * @return the output file specified.
 */
fun File.decompress(output: File = File(parent, "${nameWithoutExtension}_decompressed.$extension")): File {
    FileZipper.decompress(this.toPath(), output.toPath())
    return output
}

/**
 * Decompresses / Unzips a file.
 *
 * @param output the file where we want the uncompressed archive to be.
 * @return the output file specified.
 */
fun AdvancedFile.decompress(
    output: AdvancedFile = AdvancedFile(
        path.parent.resolve("${this.filenameWithoutExtension}_decompressed.${FileUtils.getExtension(path)}")
    )
): AdvancedFile {
    FileZipper.decompress(path, output.path)
    return output
}

/**
 * Returns the child of the advanced file thru a getter-style operator.
 *
 * @param file name of the file in the folder.
 * @return child of folder with the specified name.
 */
operator fun AdvancedFile.get(file: String): AdvancedFile = getChild(file).asAdvancedFile()


/**
 * Returns the child of the advanced file thru a getter-style operator.
 *
 * @param file name of the file in the folder.
 * @return child of folder with the specified name.
 */
operator fun File.get(file: String): File = File(this, file)

/**
 * Creates this file if it does not exist.
 *
 * @return the same instance of the file after creation.
 */
operator fun File.invoke(): File {
    createNewFile()
    return this
}

/**
 * Writes to the file the content provided.
 * Does not overwrite the file, but rather append to it.
 *
 * @param content the content which we want to append to the file.
 */
operator fun AdvancedFile.plusAssign(content: String) = appendToFile(content)

/**
 * Checks whether the file specified exists in the folder provided.
 *
 * @param file the name of the file which we want to check.
 * @return whether the specified file exists.
 */
operator fun AdvancedFile.contains(file: String): Boolean = Files.exists(getChild(file))

/**
 * Checks whether the file specified exists in the folder provided.
 *
 * @param file the name of the file which we want to check.
 * @return whether the specified file exists.
 */
operator fun File.contains(file: String): Boolean = this[file].exists()

/**
 * Checks whether the two files content is the same.
 *
 * @param file the file which we want to check against.
 * @return whether they are the same.
 */
infix fun File.sameAs(file: File): Boolean =
    Files.mismatch(toPath(), file.toPath()) == -1L

/**
 * Checks whether the two files content is the same.
 *
 * @param file the file which we want to check against.
 * @return whether they are the same.
 */
infix fun AdvancedFile.sameAs(otherFile: AdvancedFile): Boolean =
    Files.mismatch(path, otherFile.path) == -1L

/**
 * Just an alias for sameAs.
 *
 * @param file the file which we want to check against.
 * @return whether they are the same.
 */
infix fun File.same(file: File): Boolean =
    this sameAs file

/**
 * Just an alias for sameAs.
 *
 * @param file the file which we want to check against.
 * @return whether they are the same.
 */
infix fun AdvancedFile.same(otherFile: AdvancedFile): Boolean =
    this sameAs otherFile