package net.iceyleagons.gradle.catalogs

import net.iceyleagons.gradle.utils.UpdateType
import com.amihaiemil.eoyaml.Yaml
import com.amihaiemil.eoyaml.YamlMappingBuilder
import com.amihaiemil.eoyaml.YamlSequenceBuilder
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Handles everything configuration related.
 */
data class IcicleConfiguration(
    /**
     * The name of the Icicle module.
     */
    var name: String = "undefined",
    /**
     * Whether to register the icicle.yml generation task.
     */
    var registerIcicleTask: Boolean = true,
    /**
     * Whether to register the "shadow" configuration.
     */
    var registerShadow: Boolean = true,
    /**
     * Whether to register the "runtimeDownlaod" and the "icicleAddon" configuration.
     */
    var registerRuntimeDownload: Boolean = true,
    /**
     * Whether to automatically generate an icicle.yml in the final jar file.
     *
     * **DEPENDENT ON THE [registerIcicleTask] FIELD!**
     */
    var generateIcicleYml: Boolean = true,
    /**
     * Whether to modify/create a plugin.yml so that it contains Icicle as its dependencies.
     */
    var modifyPluginYml: Boolean = true,
    /**
     * Whether to force the project to use UTF-8. This resolves some encoding related issues.
     */
    var forceUtf8: Boolean = true,
    /**
     * Whether to register minecraft-specific dependencies as an extension.
     */
    var minecraftDependencies: Boolean = true,
    /**
     * Whether to register repositories into the "repos" extension.
     */
    var registerRepositories: Boolean = true,
    /**
     * The list of dependencies that will be downloaded at runtime.
     */
    internal val dependencies: Map<String, String> = HashMap(8),
    /**
     * The list of ICICLE MODULES that will be downloaded at runtime.
     */
    internal val icicle_dependencies: Map<String, String> = HashMap(4),
    /**
     * The class which you want to be called when an update is to be checked.
     */
    var updater_class: String? = null,
    /**
     * The update behaviour of this module.
     *
     * **If set to anything other than [UpdateType.IGNORE] then the [updater_class] field NEEDS to be set!**
     */
    var dependency_update: UpdateType = UpdateType.IGNORE
) {
    /**
     * Writes the content of this configuration into a file.
     *
     * @param file the file in which we want to write the contents of this data class.
     * @return whether the procedure succeeded.
     */
    internal fun writeToFile(file: File): Boolean {
        var yamlBuilder: YamlMappingBuilder = Yaml.createYamlMappingBuilder().add("name", name)

        if (icicle_dependencies.isNotEmpty()) {
            var mapping = Yaml.createYamlMappingBuilder()
            val repositoryMap: MutableMap<String, YamlSequenceBuilder> = HashMap(4)
            for ((repository, dependency) in icicle_dependencies) {
                var sb = repositoryMap.getOrDefault(repository, Yaml.createYamlSequenceBuilder())
                sb = sb.add(dependency)
                repositoryMap[repository] = sb
            }

            for ((key, value) in repositoryMap) mapping = mapping.add(key, value.build())
            yamlBuilder = yamlBuilder.add("icicle-dependencies", mapping.build())
        }

        if (dependencies.isNotEmpty()) {
            var mapping = Yaml.createYamlMappingBuilder()
            val repositoryMap: MutableMap<String, YamlSequenceBuilder> = HashMap(4)
            for ((repository, dependency) in dependencies) {
                var sb = repositoryMap.getOrDefault(repository, Yaml.createYamlSequenceBuilder())
                sb = sb.add(dependency)
                repositoryMap[repository] = sb
            }

            for ((key, value) in repositoryMap) mapping = mapping.add(key, value.build())
            yamlBuilder = yamlBuilder.add("dependencies", mapping.build())
        }

        if (dependency_update != UpdateType.IGNORE) {
            yamlBuilder = yamlBuilder.add("update-class", updater_class)
            yamlBuilder = yamlBuilder.add("update-type", dependency_update.id.toString())
        }

        file.writer(StandardCharsets.UTF_8).use {
            it.write(yamlBuilder.build().toString())
        }
        return true
    }

    /**
     * The dependency notation of icicle-core.
     */
    val core: String
        get() = "net.iceyleagons:icicle-core:1.0.0"

    operator fun get(name: String): String = "net.iceyleagons:icicle-$name:1.0.0"
    operator fun invoke(name: String): String = get(name)
    operator fun invoke(): String = core
}