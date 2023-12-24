package cloud.drakon.dynamisbot.lib.discord.interaction.applicationcommand

import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-application-command-interaction-data-option-structure
@Serializable
internal class ApplicationCommandInteractionDataOption(
    val name: String,
    val type: Byte,
    val value: String? = null, // TODO: Should be a Union of String, Int, Double, and Boolean
    val options: Array<ApplicationCommandInteractionDataOption>,
)
