package net.iceyleagons.addons.database

import org.jetbrains.exposed.sql.Table

object UploaderTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 32)
    val acceptedDeveloperNames = text("devnames")
    val type = short("type")
    val token = text("token")

    override val primaryKey = PrimaryKey(id, name = "PK_uploader_ID")
}