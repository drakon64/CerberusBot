package cloud.drakon.dynamisbot.lib.discord.applicationcommand

import cloud.drakon.dynamisbot.lib.discord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-structure
@Serializable
internal class ApplicationCommandOption(
    val type: Byte,
    val name: String,
    @SerialName("name_localizations") val nameLocalizations: Map<Locale, String>? = null,
    val description: String,
    @SerialName("description_localizations") val descriptionLocalizations: Map<Locale, String>? = null,
    val required: Boolean? = null,
    val choices: Array<ApplicationCommandOptionChoice>? = null,
    val options: Array<ApplicationCommandOption>? = null,
)
