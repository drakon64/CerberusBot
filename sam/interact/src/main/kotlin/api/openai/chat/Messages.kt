package cloud.drakon.tempestbot.interact.api.openai.chat

import kotlinx.serialization.Serializable

@Serializable class Messages(val messages: Array<Message>)
