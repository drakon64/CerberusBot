package cloud.drakon.dynamisbot.lib.discord

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/reference#locales
@Serializable
internal enum class Locale(val aws: String) {
    @SerialName("ja")
    JAPANESE("ja"),

    @SerialName("de")
    GERMAN("de"),

    @SerialName("fr")
    FRENCH("fr"),
}
