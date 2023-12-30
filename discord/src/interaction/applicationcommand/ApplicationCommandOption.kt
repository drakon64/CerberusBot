package cloud.drakon.ktdiscord.interaction.applicationcommand

import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommandOptionChoice
import cloud.drakon.ktdiscord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-structure
@Serializable
data class ApplicationCommandOption(
    val type: Byte,
    val name: String,
    @SerialName("name_localizations") val nameLocalizations: Map<Locale, String>? = null,
    val description: String,
    @SerialName("description_localizations") val descriptionLocalizations: Map<Locale, String>? = null,
    val required: Boolean? = null,
    val choices: List<ApplicationCommandOptionChoice>? = null,
    val options: List<ApplicationCommandOption>? = null,
)
