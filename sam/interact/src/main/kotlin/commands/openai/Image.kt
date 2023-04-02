package cloud.drakon.tempestbot.interact.commands.openai

import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscordClient
import cloud.drakon.tempestbot.interact.Handler.Companion.openAi
import cloud.drakon.tempestbot.interact.api.openai.images.ImageRequest
import java.util.Base64

suspend fun image(event: Interaction<ApplicationCommandData>) {
    lateinit var prompt: String

    for (i in event.data !!.options !!) {
        when (i.name) {
            "prompt" -> prompt = i.value !!
        }
    }

    val createdImage =
        Base64.getDecoder()
            .decode(openAi.createImage(ImageRequest(prompt)).data[0].b64Json)

    ktDiscordClient.editOriginalInteractionResponse(
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
