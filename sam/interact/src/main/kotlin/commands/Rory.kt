package cloud.drakon.tempestbot.interact.commands

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun rory(event: Interaction<ApplicationCommandData>) {
    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            Json.parseToJsonElement(
                HttpClient(Java).get("https://rory.cat/purr").body()
            ).jsonObject["url"] !!.jsonPrimitive.content
        ), event.token
    )
}
