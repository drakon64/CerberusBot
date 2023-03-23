package cloud.drakon.tempestbot.interact.api.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ChatRequest(
    val model: String,
    val messages: Array<Message>,
    val temperature: Double? = null,
    @SerialName("top_p") val topP: Double? = null,
    val n: Byte? = null,
    val stream: Boolean? = null,
    val stop: String? = null,
    @SerialName("max_tokens") val maxTokens: Short? = null,
    @SerialName("presence_penalty") val presencePenalty: Double? = null,
    @SerialName("frequency_penalty") val frequencyPenalty: Double? = null,
)
