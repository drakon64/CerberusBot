package cloud.drakon.dynamisbot.lib.discord.interaction.applicationcommand

import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-application-command-interaction-data-option-structure
@Serializable
class ApplicationCommandInteractionDataOption(
    val name: String,
    val type: Byte,
    val value: String? = null, // TODO: Should be a Union of String, Int, Double, and Bool
    val options: Array<ApplicationCommandInteractionDataOption>,
)
