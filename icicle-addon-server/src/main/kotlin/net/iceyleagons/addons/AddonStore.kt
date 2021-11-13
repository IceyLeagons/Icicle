package net.iceyleagons.addons

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.FileReader

class AddonStore {
    private val pepper: String
    private val hasher = BCrypt.withDefaults()
    private val verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2A)

    private val debug1 = false

    init {
        if (File("pepper.txt").exists())
            FileReader(File("pepper.txt")).use {
                pepper = it.readText()
            }
        else pepper = "default_pepper_123_"

        Database.connect("jdbc:sqlite:database.db", driver = "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(AddonDataTable)
            SchemaUtils.create(UploaderTable)

            if (debug1) {
                UploaderTable.insertIgnore {
                    it[name] = "iceyleagons"
                    it[type] = 1
                    it[acceptedDeveloperNames] = "IceyLeagons;Gabe;TOTHTOMI"
                    it[token] = "verYVERYRanODM23121TOKENPASSHASH".hash().also { a -> println(a) }
                }

                AddonDataTable.insertIgnore {
                    it[name] = "test"
                    it[title] = "Test Addon"
                    it[notation] = "icicle:test"
                    it[description] = "This is a test addon just for testing the database."
                    it[latestVersion] = "1.0.0-ALPHA"
                    it[developer] = "IceyLeagons"
                    it[downloadLink] = "https://mvn.iceyleagons.net/net/iceyleagons/test/{version}.jar"
                    it[website] = "https://iceyleagons.net/"
                    it[minIcicleVersion] = "0.1-ALPHA"
                    it[type] = 2
                    it[tags] = "test, addon, icicle, random1, random2"
                    it[dependencies] = "icicle:core:0.1-ALPHA, icicle:utilities:0.1-ALPHA"
                }

                AddonDataTable.insertIgnore {
                    it[name] = "core"
                    it[title] = "Core"
                    it[notation] = "icicle:core"
                    it[description] = "This is the core of icicle. Required for EVERY icicle dependency."
                    it[latestVersion] = "0.1-ALPHA"
                    it[developer] = "IceyLeagons"
                    it[downloadLink] = "https://mvn.iceyleagons.net/net/iceyleagons/core/{version}.jar"
                    it[website] = "https://iceyleagons.net/"
                    it[minIcicleVersion] = "NONE"
                    it[type] = 1
                    it[tags] = "icicle, core, utility, official"
                    it[dependencies] = "icicle:utilities:0.1-ALPHA"
                }

                AddonDataTable.insertIgnore {
                    it[name] = "utility"
                    it[title] = "Utilities"
                    it[notation] = "icicle:utilities"
                    it[description] =
                        "This is the part of Icicle that provides most utilities. Shipped by default in core."
                    it[latestVersion] = "0.1-ALPHA"
                    it[developer] = "IceyLeagons"
                    it[downloadLink] = "https://mvn.iceyleagons.net/net/iceyleagons/utilities/{version}.jar"
                    it[website] = "https://iceyleagons.net/"
                    it[minIcicleVersion] = "0.1-ALPHA"
                    it[type] = 1
                    it[tags] = "icicle, core, utility, official, bundled"
                    it[dependencies] = "icicle:core:0.1-ALPHA"
                }
            }
        }
    }


    fun tryUploadAddon(addonData: AddonData, token: String): Int {
        return transaction {
            val uploader =
                UploaderTable.select { UploaderTable.token eq token }.toList().getOrNull(0) ?: return@transaction -401

            if (!uploader[UploaderTable.acceptedDeveloperNames].lowercase().split(";")
                    .contains(addonData.developer.lowercase())
            )
                return@transaction -405

            val maxType = uploader[UploaderTable.type]

            val existingAddon =
                AddonDataTable.select { (AddonDataTable.name.lowerCase() eq addonData.name.lowercase()) and (AddonDataTable.developer.lowerCase() eq addonData.developer.lowercase()) }
                    .toList().getOrNull(0)
            if (existingAddon != null) {
                existingAddon[AddonDataTable.title] = addonData.title
                existingAddon[AddonDataTable.description] = addonData.description
                existingAddon[AddonDataTable.latestVersion] = addonData.latestVersion
                existingAddon[AddonDataTable.downloadLink] = addonData.downloadLink
                existingAddon[AddonDataTable.website] = addonData.website
                existingAddon[AddonDataTable.minIcicleVersion] = addonData.minIcicleVersion
                existingAddon[AddonDataTable.tags] = addonData.tags
                existingAddon[AddonDataTable.dependencies] = addonData.dependencies
                // Update existing addon
                return@transaction existingAddon[AddonDataTable.id]
            } else {
                // Upload new addon.
                val result = AddonDataTable.insert {
                    it[name] = addonData.name.lowercase()
                    it[title] = addonData.title
                    it[notation] = "${
                        addonData.developer.lowercase().replace("[^A-Za-z0-9]".toRegex(), "-")
                    }:${addonData.name.lowercase()}"
                    it[description] = addonData.description
                    it[latestVersion] = addonData.latestVersion
                    it[developer] = addonData.developer
                    it[downloadLink] = addonData.downloadLink
                    it[website] = addonData.website
                    it[minIcicleVersion] = addonData.minIcicleVersion
                    it[type] = if (addonData.type < maxType) maxType else addonData.type
                    it[tags] = addonData.tags
                    it[dependencies] = addonData.dependencies
                }.resultedValues?.getOrNull(0)
                return@transaction result?.get(AddonDataTable.id) ?: -500
            }

        }
    }

    private infix fun String.same(other: String): Boolean =
        verifier.verify((pepper + this).toCharArray(), other.toCharArray()).verified

    private fun String.hash(): String {
        return hasher.hashToString(12, (pepper + this).toCharArray())
    }

    private fun ResultRow.mapToAddonData(): AddonData {
        val it = this
        val id = it[AddonDataTable.id]
        val name = it[AddonDataTable.name]
        val type = it[AddonDataTable.type]
        val tags = it[AddonDataTable.tags]
        val title = it[AddonDataTable.title]
        val desc = it[AddonDataTable.description]
        val latest = it[AddonDataTable.latestVersion]
        val dev = it[AddonDataTable.developer]
        val download = it[AddonDataTable.downloadLink].replace("{version}", latest)
        val website = it[AddonDataTable.website]
        val minVer = it[AddonDataTable.minIcicleVersion]
        val dep = it[AddonDataTable.dependencies]

        return AddonData(
            id = id,
            name = name,
            description = desc,
            latestVersion = latest,
            developer = dev,
            downloadLink = download,
            website = website,
            minIcicleVersion = minVer,
            dependencies = dep,
            title = title,
            tags = tags,
            type = type
        )
    }

    private fun ResultRow.mapToReducedAddonData(): ReducedAddonData {
        val it = this
        val id = it[AddonDataTable.id]
        val name = it[AddonDataTable.name]
        val type = it[AddonDataTable.type]
        val tags = it[AddonDataTable.tags]
        val title = it[AddonDataTable.title]
        val description = it[AddonDataTable.description]
        val developer = it[AddonDataTable.developer]
        val website = it[AddonDataTable.website]

        return ReducedAddonData(
            id = id,
            name = name,
            title = title,
            developer = developer,
            description = description,
            type = type,
            tags = tags,
            website = website
        )
    }

    fun getAddonAmountByDeveloper(developer: String): Int {
        return transaction {
            AddonDataTable.select {
                AddonDataTable.developer like "%$developer%"
            }.toList().size
        }
    }

    fun getAddonAmountByTags(tags: String): Int {
        return transaction {
            AddonDataTable.select {
                val split = tags.split("/")
                if (split.size == 1)
                    AddonDataTable.tags like "%$tags%"
                else {
                    var op: Op<Boolean> =
                        (AddonDataTable.tags like "%${split[0]}%") and (AddonDataTable.tags like "%${split[1]}%")
                    split.forEachIndexed { index, part ->
                        if (index == 0 || index == 1)
                            return@forEachIndexed

                        op = op and (AddonDataTable.tags like "%$part%")
                    }

                    op
                }
            }.toList().size
        }
    }

    fun getAddonAmountByTitle(title: String): Int {
        return transaction {
            AddonDataTable.select {
                AddonDataTable.title like "%$title%"
            }.toList().size
        }
    }

    fun getAddonAmountByType(type: Short): Int {
        return transaction {
            AddonDataTable.select {
                AddonDataTable.type eq type
            }.toList().size
        }
    }

    fun searchAddonsPageByDeveloper(developer: String, perPage: Int, page: Int): List<ReducedAddonData> {
        val actualPerPage = perPage.coerceAtLeast(10).coerceAtMost(50)
        return transaction {
            AddonDataTable.select {
                AddonDataTable.developer like "%$developer%"
            }.limit(actualPerPage, page.coerceAtLeast(0).times(actualPerPage).toLong()).toList().map {
                it.mapToReducedAddonData()
            }
        }
    }

    fun searchAddonsPageByTags(tags: String, perPage: Int, page: Int): List<ReducedAddonData> {
        val actualPerPage = perPage.coerceAtLeast(10).coerceAtMost(50)
        return transaction {
            AddonDataTable.select {
                val split = tags.split("/")
                if (split.size == 1)
                    AddonDataTable.tags like "%$tags%"
                else {
                    var op: Op<Boolean> =
                        (AddonDataTable.tags like "%${split[0]}%") and (AddonDataTable.tags like "%${split[1]}%")
                    split.forEachIndexed { index, part ->
                        if (index == 0 || index == 1)
                            return@forEachIndexed

                        op = op and (AddonDataTable.tags like "%$part%")
                    }

                    op
                }
            }.limit(actualPerPage, page.coerceAtLeast(0).times(actualPerPage).toLong()).toList().map {
                it.mapToReducedAddonData()
            }
        }
    }

    fun searchAddonsPageByTitle(title: String, perPage: Int, page: Int): List<ReducedAddonData> {
        val actualPerPage = perPage.coerceAtLeast(10).coerceAtMost(50)
        return transaction {
            AddonDataTable.select {
                AddonDataTable.title like "%$title%"
            }.limit(actualPerPage, page.coerceAtLeast(0).times(actualPerPage).toLong()).toList().map {
                it.mapToReducedAddonData()
            }
        }
    }

    fun searchAddonsPageByType(type: Short, perPage: Int, page: Int): List<ReducedAddonData> {
        val actualPerPage = perPage.coerceAtLeast(10).coerceAtMost(50)
        return transaction {
            AddonDataTable.select {
                AddonDataTable.type eq type
            }.limit(actualPerPage, page.coerceAtLeast(0).times(actualPerPage).toLong()).toList().map {
                it.mapToReducedAddonData()
            }
        }
    }

    fun getAddonsPage(perPage: Int, page: Int): List<ReducedAddonData> {
        val actualPerPage = perPage.coerceAtLeast(10).coerceAtMost(50)
        return transaction {
            AddonDataTable.selectAll().limit(actualPerPage, page.coerceAtLeast(0).times(actualPerPage).toLong()).map {
                it.mapToReducedAddonData()
            }
        }
    }

    fun getAddonFromId(id: Int): AddonData? {
        return transaction {
            return@transaction AddonDataTable.select {
                AddonDataTable.id eq id
            }.toList().map {
                it.mapToAddonData()
            }.getOrNull(0)
        }
    }

    fun getAddon(name: String): AddonData? {
        return transaction {
            return@transaction AddonDataTable.select {
                AddonDataTable.name eq name
            }.toList().map {
                it.mapToAddonData()
            }.getOrNull(0)
        }
    }

}