package net.iceyleagons.gradle.catalogs

import net.iceyleagons.gradle.IciclePlugin
import org.gradle.api.Action
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

open class RepositoryCatalog {
    val igloo: MavenArtifactRepository
        get() =
            IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
                override fun execute(t: MavenArtifactRepository) {
                    t.url = URI("https://mvn.iceyleagons.net/releases/")
                    t.name = "Icicle Igloo"
                }
            })

    val iglooSnapshots: MavenArtifactRepository
        get() = IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://mvn.iceyleagons.net/snapshots/")
                t.name = "Icicle Igloo Snapshots"
            }
        })

    val sonatype: MavenArtifactRepository
        get() = IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://oss.sonatype.org/content/repositories/snapshots/")
                t.name = "Sonatype Snapshots"
            }
        })

    val codemc: MavenArtifactRepository
        get() = IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://repo.codemc.io/repository/nms/")
                t.name = "CodeMC"
            }
        })

    val spigot: MavenArtifactRepository
        get() = IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
                t.name = "SpigotMC Snapshots"
            }
        })

    val paper: MavenArtifactRepository
        get() = IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://papermc.io/repo/repository/maven-public/")
                t.name = "Paper Public Repo"
            }
        })

    val jitpack: MavenArtifactRepository
        get() = IciclePlugin.PROJ.repositories.maven(object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://jitpack.io/")
                t.name = "Jitpack"
            }
        })
}