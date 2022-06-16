package net.iceyleagons.icicle.extensions

import net.iceyleagons.icicle.protocol.action.Settings

/**
 * Creates a new settings builder object and returns a protocol settings object.
 *
 * @return protocol settings.
 */
fun newSettings(settingsLambda: SettingsBuilder.() -> Unit): Settings {
    val settings = SettingsBuilder()
    settingsLambda.invoke(settings)
    val rs = Settings.create()
    rs.settings.putAll(settings.settings)
    return rs
}

/**
 * The class responsible for the lambda-like creation of Protocol settings.
 * @author Gábe
 * @since 0.1
 */
class SettingsBuilder {
    val settings = HashMap<Int, Any>()

    fun field(field: Int, value: Any) {
        settings[field] = value
    }
}