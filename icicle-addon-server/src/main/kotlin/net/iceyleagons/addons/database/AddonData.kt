/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.addons.database

import org.jetbrains.exposed.sql.ResultRow
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
) {
    companion object {
        fun mapToAddonData(result: ResultRow): AddonData {
            val id = result[AddonDataTable.id]
            val name = result[AddonDataTable.name]
            val type = result[AddonDataTable.type]
            val tags = result[AddonDataTable.tags]
            val title = result[AddonDataTable.title]
            val desc = result[AddonDataTable.description]
            val latest = result[AddonDataTable.latestVersion]
            val dev = result[AddonDataTable.developer]
            val download = result[AddonDataTable.downloadLink].replace("{version}", latest)
            val website = result[AddonDataTable.website]
            val minVer = result[AddonDataTable.minIcicleVersion]
            val dep = result[AddonDataTable.dependencies]

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
    }
}

data class ReducedAddonData(
    val id: Int,
    val name: String,
    val title: String,
    val developer: String,
    val description: String,
    val type: Short,
    val tags: String,
    val website: String
) {
    companion object {
        fun mapToReducedAddonData(result: ResultRow): ReducedAddonData {
            val id = result[AddonDataTable.id]
            val name = result[AddonDataTable.name]
            val type = result[AddonDataTable.type]
            val tags = result[AddonDataTable.tags]
            val title = result[AddonDataTable.title]
            val description = result[AddonDataTable.description]
            val developer = result[AddonDataTable.developer]
            val website = result[AddonDataTable.website]

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
    }
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