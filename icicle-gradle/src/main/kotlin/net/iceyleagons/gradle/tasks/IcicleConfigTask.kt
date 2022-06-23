package net.iceyleagons.gradle.tasks

import net.iceyleagons.gradle.catalogs.IcicleConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.file.Deleter
import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import javax.inject.Inject

class IcicleConfigTask : DefaultTask() {

    @OutputDirectory
    val outputDirectory: DirectoryProperty

    @Input
    lateinit var data: IcicleConfiguration

    init {
        val objectFactory = project.objects
        outputDirectory = objectFactory.directoryProperty()
    }

    @Inject
    protected fun getDeleter(): Deleter {
        throw UnsupportedOperationException("Decorator takes care of injection")
    }

    @TaskAction
    fun generateIcicleConfig() {
        // Clean output directory
        val outputDir = outputDirectory.get().asFile
        clearOutputDirectory(outputDir)

        // Write contents of the icicle.yml file.
        data.writeToFile(File(outputDir, "icicle.yml"))
    }

    private fun clearOutputDirectory(directoryToClear: File) {
        try {
            getDeleter().ensureEmptyDirectory(directoryToClear)
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

}