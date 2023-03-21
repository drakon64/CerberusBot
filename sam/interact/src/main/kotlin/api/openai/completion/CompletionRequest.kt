package cloud.drakon.tempestbot.interact.api.openai.completion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class CompletionRequest(
    val model: String,
    val prompt: String,
    val suffix: String? = null,
    @SerialName("max_tokens") val maxTokens: Short = 16,
    val temperature: Double = 1.0,
    @SerialName("top_p") val topP: Double = 1.0,
    val n: Byte = 1,
    val stream: Boolean = false,
    @SerialName("logprobs") val logProbs: Byte? = null,
    val echo: Boolean = false,
    val stop: String? = null,
    @SerialName("presence_penalty") val presencePenalty: Double = 0.0,
    @SerialName("frequency_penalty") val frequencyPenalty: Double = 0.0,
    @SerialName("best_of") val bestOf: Int = 1,
    @SerialName("logit_bias") val logitBias: Map<Int, Short>? = null,
)
