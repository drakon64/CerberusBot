package cloud.drakon.dynamisbot.lib.discord.interaction

import cloud.drakon.dynamisbot.lib.discord.interaction.applicationcommand.ApplicationCommandData
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-interaction-structure
@Serializable
internal class Interaction(val type: Byte, val data: ApplicationCommandData? = null)
