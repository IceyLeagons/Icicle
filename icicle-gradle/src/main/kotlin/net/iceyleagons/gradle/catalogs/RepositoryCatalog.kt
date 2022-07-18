package net.iceyleagons.gradle.catalogs

import org.gradle.api.Action
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

open class RepositoryCatalog {
    val igloo: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://mvn.iceyleagons.net/releases/")
                t.name = "Icicle Igloo"
            }
        }

    val iglooSnapshots: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://mvn.iceyleagons.net/snapshots/")
                t.name = "Icicle Igloo Snapshots"
            }
        }

    val sonatype: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://oss.sonatype.org/content/repositories/snapshots/")
                t.name = "Sonatype Snapshots"
            }
        }

    val codemc: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://repo.codemc.io/repository/nms/")
                t.name = "CodeMC"
            }
        }

    val spigot: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
                t.name = "SpigotMC Snapshots"
            }
        }

    val paper: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://papermc.io/repo/repository/maven-public/")
                t.name = "Paper Public Repo"
            }
        }

    val jitpack: Action<in MavenArtifactRepository>
        get() = object : Action<MavenArtifactRepository> {
            override fun execute(t: MavenArtifactRepository) {
                t.url = URI("https://jitpack.io/")
                t.name = "Jitpack"
            }
        }
}