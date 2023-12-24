package cloud.drakon.dynamisbot.lib.discord

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/reference#locales
@Serializable
internal enum class Locale {
    @SerialName("ja") JAPANESE,
    @SerialName("en-GB") ENGLISH_UK,
    @SerialName("en-US") ENGLISH_US,
    @SerialName("de") GERMAN,
    @SerialName("fr") FRENCH,
    @SerialName("zh-CN") CHINESE_CHINA,
    @SerialName("zh-TW") CHINESE_TAIWAN,
    @SerialName("ko") KOREAN,
}
