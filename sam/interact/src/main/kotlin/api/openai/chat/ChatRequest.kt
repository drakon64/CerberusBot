package cloud.drakon.tempestbot.interact.api.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ChatRequest(
    val model: String,
    val messages: Array<Message>,
    val temperature: Double = 1.0,
    @SerialName("top_p") val topP: Double = 1.0,
    val n: Byte = 1,
    val stream: Boolean = false,
    val stop: String? = null,
    @SerialName("max_tokens") val maxTokens: Short = 2000,
    @SerialName("presence_penalty") val presencePenalty: Double = 0.0,
    @SerialName("frequency_penalty") val frequencyPenalty: Double = 0.0,
)
