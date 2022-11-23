package cloud.drakon.tempestbot.plugins

import cloud.drakon.tempest.TempestClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.request.receiveText

const val pluginName: String = "DiscordInteractionsPlugin"

val DiscordInteractionsPlugin = createApplicationPlugin(name = pluginName) {
    val tempestClient = TempestClient(
        environment !!.config.property("tempestbot.application_id").toString(),
        environment !!.config.property("tempestbot.bot_token").toString(),
        environment !!.config.property("tempestbot.public_key").toString()
    )

    onCall { call ->
        fun invalidRequestSignature() {
            call.response.status(
                HttpStatusCode(401, "invalid request signature")
            )
        }

        if (call.request.headers["X-Signature-Timestamp"] != null && call.request.headers["X-Signature-Ed25519"] != null) {
            if (! tempestClient.validateRequest(
                    call.request.headers["X-Signature-Timestamp"] !!,
                    call.receiveText(),
                    call.request.headers["X-Signature-Ed25519"] !!
                )
            ) {
                invalidRequestSignature()
            }
        } else {
            invalidRequestSignature()
        }
    }

    println("$pluginName is installed!")
}
