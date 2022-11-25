package cloud.drakon.tempestbot.plugins

import cloud.drakon.tempest.TempestClient
import cloud.drakon.tempest.interaction.InteractionType
import cloud.drakon.tempest.interaction.response.InteractionCallbackType
import cloud.drakon.tempest.interaction.response.InteractionResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

const val pluginName: String = "DiscordInteractionsPlugin"

val DiscordInteractionsPlugin = createApplicationPlugin(name = pluginName) {
    val tempestClient = TempestClient(
        environment !!.config.property("tempestbot.application_id").getString(),
        environment !!.config.property("tempestbot.bot_token").getString(),
        environment !!.config.property("tempestbot.public_key").getString()
    )

    onCall { call ->
        fun invalidRequestSignature() {
            call.response.status(
                HttpStatusCode(401, "invalid request signature")
            )
            println("Invalid request signature")
        }

        val timestamp: String? = call.request.headers["X-Signature-Timestamp"]
        val ed25519: String? = call.request.headers["X-Signature-Ed25519"]

        if (timestamp != null && ed25519 != null) {
            val body: String = call.receiveText()

            if (! tempestClient.validateRequest(
                    timestamp, body, ed25519
                )
            ) {
                invalidRequestSignature()
            } else if (Json.parseToJsonElement(body).jsonObject["type"] !!.jsonPrimitive.int == InteractionType.PING.toInt()) {
                call.respond(InteractionResponse(InteractionCallbackType.PONG))
                println("Received PING")
            }
        } else {
            invalidRequestSignature()
        }
    }

    println("$pluginName is installed!")
}
