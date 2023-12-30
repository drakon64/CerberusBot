package cloud.drakon.ktdiscord.webhook

import kotlinx.serialization.Serializable

@Serializable
data class EditWebhookMessage(val content: String? = null)
