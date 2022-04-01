package net.iceyleagons.icicle.extensions

import net.iceyleagons.icicle.protocol.action.Settings

fun newSettings(settingsLambda: SettingsBuilder.() -> Unit): Settings {
    val settings = SettingsBuilder()
    settingsLambda.invoke(settings)
    val rs = Settings.create()
    rs.settings.putAll(settings.settings)
    return rs
}

class SettingsBuilder {
    val settings = HashMap<Int, Any>()

    fun field(field: Int, value: Any) {
        settings[field] = value
    }
}