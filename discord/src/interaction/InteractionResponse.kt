package cloud.drakon.ktdiscord.interaction

import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-response-object-interaction-response-structure
@Serializable
data class InteractionResponse(val type: Byte)
