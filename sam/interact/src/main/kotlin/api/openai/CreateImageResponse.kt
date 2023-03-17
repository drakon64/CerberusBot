package cloud.drakon.tempestbot.interact.api.openai

import kotlinx.serialization.Serializable

@Serializable class CreateImageResponse(
    val created: Int,
    val data: Array<Map<String, String>>,
)
