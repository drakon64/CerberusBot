package cloud.drakon.tempestbot.interact.api.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ChatChoice(
    val index: Int,
    val message: Message,
    @SerialName("finish_reason") val finishReason: String,
)
