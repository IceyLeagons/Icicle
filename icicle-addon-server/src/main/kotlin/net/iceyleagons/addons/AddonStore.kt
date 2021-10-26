package net.iceyleagons.addons

class AddonStore {
    // TODO: Migrate to an actual addonstore?
    val addons = listOf(
        AddonData(
            1,
            "@icicle/icicle-core",
            "IceyLeagons",
            "1.0.0",
            "https://mvn.iceyleagons.net/snapshots/icicle/core/{version}.jar"
        ),
        AddonData(
            2,
            "@icicle/icicle-database",
            "IceyLeagons",
            "1.0.0",
            "https://mvn.iceyleagons.net/snapshots/icicle/database/{version}.jar"
        ),
        AddonData(
            3,
            "@icicle/icicle-utils",
            "IceyLeagons",
            "1.0.0",
            "https://mvn.iceyleagons.net/snapshots/icicle/utils/{version}.jar"
        ),
        AddonData(
            4,
            "@icicle/icicle-kotlin",
            "IceyLeagons",
            "1.0.0",
            "https://mvn.iceyleagons.net/snapshots/icicle/kotlin/{version}.jar"
        ),
        AddonData(
            5,
            "@icicle/icicle-serialization",
            "IceyLeagons",
            "1.0.0",
            "https://mvn.iceyleagons.net/snapshots/icicle/serialization/{version}.jar"
        )
    )

    fun getAddonsPage(perPage: Int, page: Int): List<AddonData> {
        val actualPerPage = perPage.coerceAtLeast(10).coerceAtMost(50)
        return addons.subList((page * actualPerPage).coerceAtMost(addons.size), ((page + 1) * actualPerPage).coerceAtMost(addons.size))
    }

    fun getAddonFromId(id: Int): AddonData? {
        return addons.filter { it.id == id }.getOrNull(0)
    }

    fun getAddon(name: String): AddonData? {
        return addons.filter { it.name.equals(name, true) }.map { addonData: AddonData ->
            addonData.downloadLink = addonData.downloadLink.replace("{version}", addonData.latestVersion)
            return addonData
        }.getOrNull(0)
    }

}