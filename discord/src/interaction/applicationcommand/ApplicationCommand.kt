package cloud.drakon.dynamisbot.discord.interaction.applicationcommand

import cloud.drakon.dynamisbot.discord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-structure
@Serializable
class ApplicationCommand(
    val type: Byte? = null,
    val name: String,
    @SerialName("name_localizations") val nameLocalizations: Map<Locale, String>? = null,
    val description: String,
    @SerialName("description_localizations") val descriptionLocalizations: Map<Locale, String>? = null,
    val options: List<ApplicationCommandOption>? = null,
    @SerialName("dm_permission") val dmPermission: Boolean? = null,
)
