package net.iceyleagons.addons

import org.jetbrains.exposed.sql.Table

data class AddonData(
    val id: Int,
    val name: String,
    val title: String,
    val description: String,
    val latestVersion: String,
    val developer: String,
    val downloadLink: String,
    val website: String,
    val minIcicleVersion: String,
    val type: Short,
    val tags: String,
    val dependencies: String
)

data class ReducedAddonData(
    val id: Int,
    val name: String,
    val title: String,
    val developer: String,
    val description: String,
    val type: Short,
    val tags: String,
    val website: String
)

object UploaderTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 32)
    val acceptedDeveloperNames = text("devnames")
    val type = short("type")
    val token = text("token")

    override val primaryKey = PrimaryKey(id, name = "PK_uploader_ID")
}

object AddonDataTable : Table() {
    val id = integer("id").autoIncrement()
    val type = short("type")
    val notation = text("notation")
    val name = varchar("name", 32)
    val title = varchar("title", 64)
    val description = text("description")
    val latestVersion = varchar("version", 32)
    val developer = varchar("developer", 32)
    var downloadLink = text("downloadlink")
    val website = varchar("website", 64)
    val minIcicleVersion = varchar("minversion", 32)
    val tags = text("tags")
    val dependencies = text("dependencies")

    override val primaryKey = PrimaryKey(id, name = "PK_addon_ID")
}