package cloud.drakon.tempestbot.interact.api.openai.images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ImageB64Json(@SerialName("b64_json") val b64Json: String)
