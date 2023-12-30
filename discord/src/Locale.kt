package cloud.drakon.ktdiscord

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/reference#locales
@Serializable
enum class Locale(val aws: String) {
    @SerialName("ja")
    Japanese("ja"),

    @SerialName("de")
    German("de"),

    @SerialName("fr")
    French("fr"),
}
