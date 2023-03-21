package cloud.drakon.tempestbot.interact.api.openai.chat

import kotlinx.serialization.Serializable

@Serializable class Message(val role: String, val content: String)
