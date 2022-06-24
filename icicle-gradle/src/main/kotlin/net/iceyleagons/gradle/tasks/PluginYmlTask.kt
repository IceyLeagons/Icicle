package net.iceyleagons.gradle.tasks

import com.amihaiemil.eoyaml.Yaml
import com.amihaiemil.eoyaml.YamlMappingBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileWriter
import java.io.IOException

internal class PluginYmlTask : DefaultTask() {

    @OutputFile
    internal lateinit var pluginYml: File

    @TaskAction
    internal fun doTask() {
        try {
            if (pluginYml.exists()) {
                val yamlBuilder = editPluginYml(pluginYml)
                if (!pluginYml.delete() || !pluginYml.createNewFile()) return
                try {
                    FileWriter(pluginYml).use { fw -> fw.write(yamlBuilder.build().toString()) }
                } catch (exception: IOException) {
                    exception.printStackTrace()
                }
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    private fun editPluginYml(file: File): YamlMappingBuilder {
        var yamlBuilder = Yaml.createYamlMappingBuilder()
        val yaml = Yaml.createYamlInput(file).readYamlMapping()
        val existingDepends = yaml.yamlSequence("depend")
        // Copy over existing non-trivial stuff.
        for (key in yaml.keys()) {
            if (key.toString() == "depend") continue
            yamlBuilder = yamlBuilder.add(key, yaml.value(key))
        }
        if (existingDepends != null) {
            var sequenceBuilder = Yaml.createYamlSequenceBuilder()
            var found = false
            for (i in 0 until existingDepends.size()) {
                if (!existingDepends.string(i).equals("icicle", true)) continue
                found = true
            }
            if (found) for (yamlNode in existingDepends) sequenceBuilder = sequenceBuilder.add(yamlNode) else {
                for (yamlNode in existingDepends) sequenceBuilder = sequenceBuilder.add(yamlNode)
                sequenceBuilder = sequenceBuilder.add("Icicle")
            }
            yamlBuilder = yamlBuilder.add("depend", sequenceBuilder.build())
        } else yamlBuilder = yamlBuilder.add("depend", Yaml.createYamlSequenceBuilder().add("Icicle").build())
        return yamlBuilder
    }

}