package cloud.drakon.tempestbot.defer

import cloud.drakon.tempest.TempestClient
import cloud.drakon.tempest.interaction.InteractionType
import cloud.drakon.tempest.interaction.response.InteractionCallbackType
import cloud.drakon.tempest.interaction.response.InteractionResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val tempestClient = TempestClient(
    System.getenv("APPLICATION_ID"),
    System.getenv("BOT_TOKEN"),
    System.getenv("PUBLIC_KEY")
)
private val headers = mapOf("Content-Type" to "application/json")

class Handler: RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(
        event: APIGatewayV2HTTPEvent,
        context: Context,
    ): APIGatewayV2HTTPResponse = runBlocking {
        val logger = context.logger
        val response = APIGatewayV2HTTPResponse()

        if (! tempestClient.validateRequest(
                event.headers["x-signature-timestamp"] !!,
                event.body,
                event.headers["x-signature-ed25519"] !!
            )
        ) {
            logger.log("Invalid request signature")

            response.statusCode = 401
        } else when (Json.parseToJsonElement(event.body).jsonObject["type"] !!.jsonPrimitive.int) {
            InteractionType.PING.toInt() -> {
                logger.log("Received PING")

                response.body =
                    Json.encodeToString(InteractionResponse(InteractionCallbackType.PONG))
                response.statusCode = 200
            }

            InteractionType.APPLICATION_COMMAND.toInt(), InteractionType.APPLICATION_COMMAND_AUTOCOMPLETE.toInt() -> {
                logger.log("Deferring channel message")

                response.headers = headers
                response.body =
                    Json.encodeToString(InteractionResponse(InteractionCallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE))
                response.statusCode = 200
            }

            InteractionType.MESSAGE_COMPONENT.toInt(), InteractionType.MODAL_SUBMIT.toInt() -> {
                logger.log("Deferring update message")

                response.headers = headers
                response.body =
                    Json.encodeToString(InteractionResponse(InteractionCallbackType.DEFERRED_UPDATE_MESSAGE))
                response.statusCode = 200
            }
        }

        return@runBlocking response
    }
}
