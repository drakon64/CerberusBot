package cloud.drakon.dynamisbot.lib.discord.interaction

import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-response-object-interaction-response-structure
@Serializable
class InteractionResponse(val type: Byte)
