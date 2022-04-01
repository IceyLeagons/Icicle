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

package net.iceyleagons.icicle

import net.iceyleagons.icicle.extensions.*
import net.iceyleagons.icicle.protocol.action.Settings
import net.iceyleagons.icicle.utilities.file.AdvancedFile
import java.io.File

class KotlinMain {
    fun doSomeProtocolStuff() {
        newSettings {
            field(Settings.POSITION, "x, y, z")
            field(Settings.TARGET, "playerName")
        }
    }

    fun doSomeStuff() {
        val newFile = File("newFolder").asAdvancedFile()
        if (newFile.isDirectory) {
            val file = if ("this.txt" !in newFile)
                newFile.createOrGetChild("this.txt")
            else newFile["this.txt"]

            file += "This is a new line.\n"

            file.compress(AdvancedFile(File("compressed_file.gzip")))
            if (newFile["compressed_file.gzip"].isZipped())
                newFile["compressed_file.zip"].decompress(AdvancedFile(File("decompressed.txt")))

            if (newFile["decompressed.txt"] sameAs newFile["this.txt"])
                println("They're the same!")
            else println("Something went wrong!")
        }
    }
}