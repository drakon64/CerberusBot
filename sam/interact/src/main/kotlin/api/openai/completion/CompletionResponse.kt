package cloud.drakon.tempestbot.interact.api.openai.completion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class CompletionResponse(
    val id: String,
    @SerialName("object") val completionObject: String,
    val created: Int,
    val model: String,
    val choices: Array<CompletionChoice>,
    val usage: Map<String, Int>,
)
