package net.iceyleagons.gradle.catalogs

import net.iceyleagons.gradle.utils.Version


class MinecraftCatalog {
    /**
     * **Returns the dependency notation of the spigot api.**
     *
     * @param version the version for which you want to net.iceyleagons.gradle.utils.get the artifact.
     * @return a dependency notation. For example: ```"org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT"```
     */
    fun spigotApi(version: String = "1.19"): String {
        return "org.spigotmc:spigot-api:$version-R0.1-SNAPSHOT"
    }

    /**
     * **Returns the dependency notation of the paper api.**
     *
     * @param version the version for which you want to net.iceyleagons.gradle.utils.get the artifact.
     * @return a dependency notation. For example: ```"com.destroystokyo.paper:paper-api:1.19-R0.1-SNAPSHOT"```
     */
    fun paperApi(version: String = "1.19"): String {
        val version1 = Version(version)

        return if (version1 > Version("1.17.0")) "io.papermc.paper:paper-api:$version-R0.1-SNAPSHOT"
        else "com.destroystokyo.paper:paper-api:$version-R0.1-SNAPSHOT"
    }

    /**
     * **Returns the dependency notation of a spigot-based servers patched file.**
     *
     * Useful when working with *"native"* calls to the minecraft server.
     *
     * @param version the version for which you want to net.iceyleagons.gradle.utils.get the artifact.
     * @return a dependency notation. For example: ```"org.spigotmc:spigot:1.19-R0.1-SNAPSHOT"```
     */
    fun nms(version: String = "1.19"): String {
        return "org.spigotmc:spigot:$version-R0.1-SNAPSHOT"
    }
}