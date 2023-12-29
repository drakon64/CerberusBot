package cloud.drakon.dynamisbot.discord.interaction.applicationcommand

import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-application-command-data-structure
@Serializable
data class ApplicationCommandData(
    val name: String,
    val options: Array<ApplicationCommandInteractionDataOption>? = null,
)
