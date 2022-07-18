package net.iceyleagons.gradle

import net.iceyleagons.gradle.catalogs.IcicleConfiguration
import net.iceyleagons.gradle.catalogs.MinecraftCatalog
import net.iceyleagons.gradle.catalogs.RepositoryCatalog
import net.iceyleagons.gradle.tasks.IcicleConfigTask
import net.iceyleagons.gradle.tasks.PluginYmlTask
import net.iceyleagons.gradle.utils.get
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.repositories.UrlArtifactRepository
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import java.io.File
import java.net.URL

class IciclePlugin : Plugin<Project> {

    companion object {
        internal var CONF: IcicleConfiguration? = null
    }

    private lateinit var target: Project
    private lateinit var config: IcicleConfiguration

    private fun getDependencyUrls(conf: String): Sequence<Pair<String, URL?>> =
        target.configurations[conf].dependencies.asSequence().mapNotNull {
            it.run { "$group:$name:$version" } to target.repositories.mapNotNull { repo ->
                (repo as? UrlArtifactRepository)?.url
            }.flatMap { repoUrl ->
                "%s/%s/%s/%s/%s-%s".format(
                    repoUrl.toString().trimEnd('/'),
                    it.group?.replace('.', '/') ?: "", it.name, it.version,
                    it.name, it.version
                ).let { x -> listOf("$x.jar", "$x.aar") }
            }.firstNotNullOf { url ->
                kotlin.runCatching {
                    val connection = URL(url).openConnection()
                    connection.getInputStream() ?: throw Exception()
                    connection.url
                    URL(
                        connection.url.toString().replace(
                            "%s/%s/%s/%s-%s".format(
                                it.group?.replace('.', '/') ?: "", it.name, it.version,
                                it.name, it.version
                            ), ""
                        ).removeSuffix(".jar").removeSuffix(".aar")
                    )
                }.getOrNull()
            }
        }

    override fun apply(target_: Project) {
        target = target_
        setup()
        dependencies()
        config()
    }

    private fun config() {
        if (config.modifyPluginYml) {
            val pluginTask = target.tasks.register(
                Strings.TASK_NAME_PLUGIN_YML_MODIFICATION,
                PluginYmlTask::class.java,
                object : Action<PluginYmlTask> {
                    override fun execute(t: PluginYmlTask) {
                        t.description = "Modifies/Creates a plugin.yml in the resources folder of this project."
                        t.group = "Icicle addon development"

                        t.pluginYml = target.file("src/main/resources/plugin.yml")
                    }
                })

            target.tasks["processResources"].dependsOn(pluginTask)
        }

        if (!config.registerIcicleTask)
            return

        val task =
            target.tasks.register(
                Strings.TASK_NAME_ICICLE_YML,
                IcicleConfigTask::class.java,
                object : Action<IcicleConfigTask> {
                    override fun execute(t: IcicleConfigTask) {
                        t.description = "Generates the icicle.yml file for the provided addon."
                        t.group = "Icicle addon development"

                        if (config.registerRuntimeDownload) {
                            for ((dependency, repo) in getDependencyUrls(Strings.CONFIGURATION_RUNTIME_DOWNLOAD))
                                config.dependencies[dependency] = repo.toString().trimEnd('/')

                            for ((dependency, repo) in getDependencyUrls(Strings.CONFIGURATION_ICICLE_ADDON))
                                config.icicle_dependencies[dependency] = repo.toString().trimEnd('/')
                        }

                        CONF = config
                        t.outputDirectory.set(target.layout.buildDirectory.dir(t.name))
                    }
                })

        if (config.generateIcicleYml)
            target.tasks.named("processResources", Copy::class.java, object : Action<Copy> {
                override fun execute(t: Copy) {
                    t.rootSpec.addChild().let {
                        it.duplicatesStrategy = DuplicatesStrategy.EXCLUDE // Hopefully fixes the duplicate files issue.
                        it.into("/")
                        it.from(task)
                    }
                }

            })
    }

    private fun dependencies() {
        val compileOnly = target.configurations[Strings.CONFIGURATION_EXTENDED_CONFIGURATION]

        if (config.registerShadow) {
            val shadow = target.configurations.create(Strings.CONFIGURATION_SHADOW)

            target.tasks["jar"].doFirst(object : Action<Task> {
                override fun execute(t: Task) {
                    t as Jar
                    val files: MutableList<File> = ArrayList(8)

                    for (file in shadow)
                        if (file.isDirectory) files += file
                        else files += target.zipTree(file)

                    t.duplicatesStrategy = DuplicatesStrategy.INCLUDE
                    t.from(files.toTypedArray())
                }

            })
        }

        if (config.registerRuntimeDownload) {
            val runtimeDownload = target.configurations.create(Strings.CONFIGURATION_RUNTIME_DOWNLOAD)
            compileOnly.extendsFrom(runtimeDownload)

            val icicleAddon = target.configurations.create(Strings.CONFIGURATION_ICICLE_ADDON)
            compileOnly.extendsFrom(icicleAddon)
        }
    }

    private fun setup() {
        config = target.extensions.create("icicle", IcicleConfiguration::class.java)

        if (config.registerRepositories)
            target.extensions.create("repos", RepositoryCatalog::class.java)

        if (config.minecraftDependencies)
            target.extensions.create("minecraft", MinecraftCatalog::class.java)

        if (config.forceUtf8)
            target.tasks["compileJava"].doFirst(object : Action<Task> {
                override fun execute(t: Task) {
                    t as JavaCompile
                    t.options.encoding = "UTF-8"
                }

            })
    }


}