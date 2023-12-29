package cloud.drakon.dynamisbot.discord.interaction

import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommandData
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-interaction-structure
@Serializable
class Interaction(val type: Byte, val data: ApplicationCommandData? = null)
