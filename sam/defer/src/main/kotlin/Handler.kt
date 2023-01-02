package cloud.drakon.tempestbot.defer

import aws.sdk.kotlin.services.lambda.LambdaClient
import aws.sdk.kotlin.services.lambda.model.InvocationType
import aws.sdk.kotlin.services.lambda.model.InvokeRequest
import aws.sdk.kotlin.services.lambda.model.LogType
import cloud.drakon.ktdiscord.KtDiscordClient
import cloud.drakon.ktdiscord.interaction.InteractionType
import cloud.drakon.ktdiscord.interaction.response.InteractionCallbackType
import cloud.drakon.ktdiscord.interaction.response.InteractionResponse
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

class Handler: RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private val discordKtClient = KtDiscordClient(
        System.getenv("APPLICATION_ID"), System.getenv("BOT_TOKEN")
    ).Interaction(System.getenv("PUBLIC_KEY"))
    private val json = Json { ignoreUnknownKeys = true }
    private val headers = mapOf("Content-Type" to "application/json")
    private val lambdaClient = LambdaClient { region = System.getenv("AWS_REGION") }
    private val interactFunctionName = System.getenv("INTERACT_FUNCTION")

    override fun handleRequest(
        event: APIGatewayV2HTTPEvent,
        context: Context,
    ): APIGatewayV2HTTPResponse = runBlocking {
        val logger = context.logger
        val response = APIGatewayV2HTTPResponse()

        if (! discordKtClient.validateRequest(
                event.headers["x-signature-timestamp"] !!,
                event.body,
                event.headers["x-signature-ed25519"] !!
            )
        ) {
            logger.log("Invalid request signature")

            response.statusCode = 401
            return@runBlocking response
        } else when (json.parseToJsonElement(event.body).jsonObject["type"] !!.jsonPrimitive.int) {
            InteractionType.PING -> {
                logger.log("Received PING")

                response.body =
                    json.encodeToString(InteractionResponse(InteractionCallbackType.PONG))
                response.statusCode = 200

                return@runBlocking response
            }

            InteractionType.APPLICATION_COMMAND, InteractionType.APPLICATION_COMMAND_AUTOCOMPLETE -> {
                logger.log("Deferring channel message")

                response.headers = headers
                response.body =
                    json.encodeToString(InteractionResponse(InteractionCallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE))
                response.statusCode = 200
            }

            InteractionType.MESSAGE_COMPONENT, InteractionType.MODAL_SUBMIT -> {
                logger.log("Deferring update message")

                response.headers = headers
                response.body =
                    json.encodeToString(InteractionResponse(InteractionCallbackType.DEFERRED_UPDATE_MESSAGE))
                response.statusCode = 200
            }
        }

        lambdaClient.invoke(InvokeRequest {
            functionName = interactFunctionName
            logType = LogType.None
            payload = event.body.toByteArray()
            invocationType = InvocationType.Event
        })

        return@runBlocking response
    }
}
