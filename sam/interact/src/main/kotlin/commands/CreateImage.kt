package cloud.drakon.tempestbot.interact.commands

import api.openai.CreateImageRequest
import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.openai.OpenAI

suspend fun createImage(event: Interaction<ApplicationCommandData>) {
    lateinit var prompt: String

    for (i in event.data !!.options !!) {
        when (i.name) {
            "prompt" -> prompt = i.value !!
        }
    }

    val createdImage = OpenAI(System.getenv("OPENAI_API_KEY")).createImage(
        CreateImageRequest(
            prompt, 1, "1024x1024", "b64_json"
        )
    ).data[0]["b64_json"] !!.toByteArray()

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            files = arrayOf(
                File(
                    id = "0",
                    filename = "${prompt}.png",
                    contentType = "image/png",
                    bytes = createdImage
                )
            ), attachments = arrayOf(
                Attachment(
                    id = "0", filename = "${prompt}.png", ephemeral = true
                )
            )
        ), event.token
    )
}
