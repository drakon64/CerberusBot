package cloud.drakon.dynamisbot.lib.discord.applicationcommand

import cloud.drakon.dynamisbot.lib.discord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-structure
@Serializable
internal class ApplicationCommand(
    val type: Byte? = null,
    val name: String,
    @SerialName("name_localizations") val nameLocalizations: Map<Locale, String>? = null,
    val description: String,
    @SerialName("description_localizations") val descriptionLocalizations: Map<Locale, String>? = null,
    val options: List<ApplicationCommandOption>? = null,
    @SerialName("dm_permission") val dmPermission: Boolean? = null,
)
