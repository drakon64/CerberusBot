package cloud.drakon.tempestbot.interact.api.openai.completion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class CompletionChoice(
    val text: String,
    val index: Int,
    val logprobs: Byte?,
    @SerialName("finish_reason") val finishReason: String,
)
