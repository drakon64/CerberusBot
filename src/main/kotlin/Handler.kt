package cloud.drakon.tempestbot

import cloud.drakon.ktdiscord.KtDiscordClient
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.InteractionJsonSerializer
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.interaction.response.InteractionResponse
import cloud.drakon.tempestbot.commands.citations.citationHandler
import cloud.drakon.tempestbot.commands.ffxiv.lodestone.lodestoneHandler
import cloud.drakon.tempestbot.commands.ffxiv.universalis
import cloud.drakon.tempestbot.commands.rory
import cloud.drakon.tempestbot.commands.translate
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Handler: RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    companion object {
        val ktDiscordClient = KtDiscordClient(
            System.getenv("APPLICATION_ID"), System.getenv("BOT_TOKEN")
        ).Interaction(System.getenv("PUBLIC_KEY"))
        val region: String = System.getenv("AWS_REGION")

        val mongoDatabase: MongoDatabase =
            MongoClients.create(System.getenv("MONGODB_URL"))
                .getDatabase(System.getenv("MONGODB_DATABASE"))

        val json = Json {
            ignoreUnknownKeys =
                true // Not all fields returned by the Discord API are documented
            isLenient = true // TODO https://github.com/TempestProject/Tempest/issues/3
        }
    }

    override fun handleRequest(
        event: APIGatewayV2HTTPEvent,
        context: Context,
    ): APIGatewayV2HTTPResponse = runBlocking {
        val logger = context.logger
        val response = APIGatewayV2HTTPResponse()

        if (! ktDiscordClient.validateRequest(
                event.headers["x-signature-timestamp"] !!,
                event.body,
                event.headers["x-signature-ed25519"] !!
            )
        ) {
            response.statusCode = 401

            return@runBlocking response
        }

        val interaction: Interaction<*> =
            json.decodeFromString(InteractionJsonSerializer, event.body)

        when (interaction.data) {
            is ApplicationCommandData -> {
                async {
                    ktDiscordClient.createInteractionResponse(
                        interactionId = interaction.id,
                        interactionToken = interaction.token,
                        interactionResponse = InteractionResponse(type = 5)
                    )
                }

                val applicationCommand =
                    interaction as Interaction<ApplicationCommandData>
                when (applicationCommand.data !!.name) {
                    "citation", "Add citation", "Get citation" -> citationHandler(
                        applicationCommand
                    )

                    "lodestone" -> lodestoneHandler(applicationCommand)
                    "rory" -> rory(applicationCommand)
                    "translate", "Translate" -> translate(applicationCommand, logger)
                    "universalis" -> universalis(applicationCommand, logger)
                    else -> {
                        logger.log("Unknown command: " + interaction.data !!.name)
                    }
                }
            }

            else -> {
                logger.log("Unknown command type: " + interaction.javaClass)
            }
        }

        response.statusCode = 200
        return@runBlocking response
    }
}
