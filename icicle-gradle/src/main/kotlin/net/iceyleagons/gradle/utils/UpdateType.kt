package net.iceyleagons.gradle.utils

enum class UpdateType(val id: Int) {
    /**
     * Automatically checks for updates on every server launch and download the newer version.
     */
    AUTO(1),

    /**
     * Warns the administrator about a new version being available but doesn't download by itself.
     */
    WARNING(2),

    /**
     * Disables update checking altogether.
     */
    IGNORE(-1)
}