package cloud.drakon.tempestbot.interact.api.openai.images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ImageRequest(
    val prompt: String,
    val n: Byte = 1,
    val size: String = "1024x1024",
    @SerialName("response_format") val responseFormat: String = "b64_json",
)
