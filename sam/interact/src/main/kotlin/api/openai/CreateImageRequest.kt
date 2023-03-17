package api.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class CreateImageRequest(
    val prompt: String,
    val n: Byte? = null,
    val size: String? = null,
    @SerialName("response_format") val responseFormat: String? = null,
    val user: String? = null,
)
