package net.iceyleagons.gradle

import net.iceyleagons.gradle.utils.get
import net.iceyleagons.gradle.catalogs.IcicleConfiguration
import net.iceyleagons.gradle.catalogs.MinecraftCatalog
import net.iceyleagons.gradle.catalogs.RepositoryCatalog
import net.iceyleagons.gradle.tasks.IcicleConfigTask
import net.iceyleagons.gradle.tasks.PluginYmlTask
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.repositories.UrlArtifactRepository
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.internal.file.copy.CopySpecInternal
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import java.io.File
import java.net.URL

class IciclePlugin : Plugin<Project> {

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
            val pluginTask = target.tasks.register("modifyPluginYml", PluginYmlTask::class.java, object : Action<PluginYmlTask> {
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
            target.tasks.register("generateIcicleYml", IcicleConfigTask::class.java, object : Action<IcicleConfigTask> {
                override fun execute(t: IcicleConfigTask) {
                    t.description = "Generates the icicle.yml file for the provided addon."
                    t.group = "Icicle addon development"

                    t.data = config
                    t.outputDirectory.set(target.layout.buildDirectory.dir(t.name))
                }
            })

        if (config.generateIcicleYml)
            target.tasks.named("processResources", Copy::class.java, object : Action<Copy> {
                override fun execute(t: Copy) {
                    val copyIcicleFile: CopySpecInternal = t.rootSpec.addChild()
                    copyIcicleFile.into("/")
                    copyIcicleFile.from(task)
                }

            })
    }

    private fun dependencies() {
        val compileOnly = target.configurations["compileOnly"]

        if (config.registerShadow) {
            val shadow = target.configurations.create("shadow")

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
            val runtimeDownload = target.configurations.create("runtimeDownload")
            compileOnly.extendsFrom(runtimeDownload)

            val icicleAddon = target.configurations.create("icicleAddon")
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