package net.iceyleagons.icicle

import net.iceyleagons.icicle.extensions.*
import java.io.File

class KotlinMain {
    fun doSomeStuff() {
        val newFile = File("newFolder").asAdvancedFile()
        if (newFile.isFolder) {
            val file = if ("this.txt" !in newFile)
                newFile.createOrGetChild("this.txt")
            else newFile["this.txt"]

            file += "This is a new line.\n"

            file.compress(File("compressed_file.gzip"))
            if (newFile["compressed_file.gzip"].isZipped())
                newFile["compressed_file.zip"].decompress(File("decompressed.txt"))

            if (newFile["decompressed.txt"] sameAs newFile["this.txt"])
                println("They're the same!")
            else println("Something went wrong!")
        }
    }
}