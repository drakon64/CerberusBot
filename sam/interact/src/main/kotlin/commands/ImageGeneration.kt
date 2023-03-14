package cloud.drakon.tempestbot.interact.commands

import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.Handler.Companion.openAiClient
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get

@OptIn(BetaOpenAI::class)
suspend fun imageGeneration(event: Interaction<ApplicationCommandData>) {
    lateinit var prompt: String

    for (i in event.data !!.options !!) {
        when (i.name) {
            "prompt" -> prompt = i.value !!
        }
    }

    val image = openAiClient.imageURL(
        creation = ImageCreation(
            prompt = prompt, n = 4, size = ImageSize.is1024x1024
        )
    )

    val ktorClient = HttpClient(Java)
    lateinit var files: MutableList<File>
    lateinit var attachments: MutableList<Attachment>

    for ((count, i) in image.withIndex()) {
        val countString = count.toString()

        files.add(
            File(
                id = countString,
                filename = prompt + "_" + count,
                contentType = "image/png",
                bytes = ktorClient.get(i.url).body() as ByteArray
            )
        )

        attachments.add(Attachment(id = countString, filename = prompt + "_" + count))
    }
    val filesArray = files.toTypedArray()
    val attachmentsArray = attachments.toTypedArray()

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(files = filesArray, attachments = attachmentsArray),
        event.token
    )
}
