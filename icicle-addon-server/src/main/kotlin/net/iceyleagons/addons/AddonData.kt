package net.iceyleagons.addons

data class AddonData(
    val id: Int,
    val name: String,
    val developer: String,
    val latestVersion: String,
    var downloadLink: String
)