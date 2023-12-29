package cloud.drakon.dynamisbot.discord.webhook

import kotlinx.serialization.Serializable

@Serializable
data class EditWebhookMessage(val content: String? = null)
