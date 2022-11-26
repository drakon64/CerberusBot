package cloud.drakon.tempestbot

import cloud.drakon.tempest.TempestClient
import cloud.drakon.tempest.interaction.InteractionType
import cloud.drakon.tempest.interaction.response.InteractionCallbackType
import cloud.drakon.tempest.interaction.response.InteractionResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class Handler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(
        event: APIGatewayV2HTTPEvent,
        context: Context,
    ): APIGatewayV2HTTPResponse {
        val logger = context.logger
        val response = APIGatewayV2HTTPResponse()

        val tempestClient = TempestClient(
            System.getenv("APPLICATION_ID"),
            System.getenv("BOT_TOKEN"),
            System.getenv("PUBLIC_KEY")
        )

        if (! tempestClient.validateRequest(
                event.headers["x-signature-timestamp"] !!,
                event.body,
                event.headers["x-signature-ed25519"] !!
            )
        ) {
            logger.log("Invalid request signature")

            response.statusCode = 401
        } else if (Json.parseToJsonElement(event.body).jsonObject["type"] !!.jsonPrimitive.int == InteractionType.PING.toInt()) {
            logger.log("Received PING")

            response.body =
                Json.encodeToString(InteractionResponse(InteractionCallbackType.PONG))
            response.statusCode = 200
        }

        return response
    }
}
