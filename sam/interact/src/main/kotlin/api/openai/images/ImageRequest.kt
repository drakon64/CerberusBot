package cloud.drakon.tempestbot.interact.api.openai.images

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImageRequest @OptIn(ExperimentalSerializationApi::class) constructor(
    val prompt: String,
    @EncodeDefault @SerialName("response_format")
    val responseFormat: String = "b64_json",
)
