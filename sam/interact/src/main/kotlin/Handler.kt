package cloud.drakon.tempestbot.interact

import cloud.drakon.discordkt.TempestClient
import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.InteractionJsonSerializer
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempestbot.interact.commands.citations.citationHandler
import cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone.lodestoneHandler
import cloud.drakon.tempestbot.interact.commands.ffxiv.universalis
import cloud.drakon.tempestbot.interact.commands.translate
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Handler: RequestStreamHandler {
    companion object {
        val tempestClient = TempestClient(
            System.getenv("APPLICATION_ID"),
            System.getenv("BOT_TOKEN"),
            System.getenv("PUBLIC_KEY")
        )
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
        inputStream: InputStream,
        outputStream: OutputStream,
        context: Context,
    ) = runBlocking {
        val logger = context.logger

        val event: Interaction<*> = json.decodeFromString(
            InteractionJsonSerializer, inputStream.readAllBytes().decodeToString()
        )

        when (event.data) {
            is ApplicationCommandData -> {
                val applicationCommand: Interaction<ApplicationCommandData> =
                    event as Interaction<ApplicationCommandData>
                when (applicationCommand.data !!.name) {
                    "citation", "Add citation", "Get citation" -> citationHandler(
                        applicationCommand
                    )

                    "lodestone" -> lodestoneHandler(applicationCommand)

                    "translate", "Translate" -> translate(applicationCommand, logger)
                    "universalis" -> universalis(applicationCommand, logger)
                    else -> {
                        logger.log("Unknown command: " + event.data !!.name)
                    }
                }
            }

            else -> {
                logger.log("Unknown command type: " + event.javaClass)
            }
        }
    }
}
