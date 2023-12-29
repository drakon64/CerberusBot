package cloud.drakon.dynamisbot.discord

import cloud.drakon.dynamisbot.discord.webhook.EditWebhookMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

data class DiscordInteractionsClient(val applicationId: Long) {
    private val ktorClient = HttpClient {
        defaultRequest {
            url("https://discord.com/api/v10/")
            header("Authorization", "Bot ${System.getenv("DISCORD_BOT_TOKEN")}")
        }

        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun editOriginalInteractionResponse(
        interactionToken: String,
        editWebhookMessage: EditWebhookMessage,
    ) = ktorClient.patch("/webhooks/$applicationId/$interactionToken/messages/@original") {
        contentType(ContentType.Application.Json)
        setBody(editWebhookMessage)
    }
}
