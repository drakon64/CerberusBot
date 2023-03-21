package cloud.drakon.tempestbot.interact.api.openai.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ChatResponse(
    val id: String,
    @SerialName("object") val chatObject: String,
    val created: Int,
    val model: String,
    val choices: Array<ChatChoice>,
    val usage: ChatUsage,
)
