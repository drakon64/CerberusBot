package cloud.drakon.dynamisbot.lib.discord.interaction.applicationcommand

import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-application-command-data-structure
@Serializable
class ApplicationCommandData(
    val name: String,
    val options: Array<ApplicationCommandInteractionDataOption>,
)
