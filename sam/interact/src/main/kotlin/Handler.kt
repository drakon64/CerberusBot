package cloud.drakon.tempestbot.interact

import cloud.drakon.ktdiscord.KtDiscord
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.InteractionJsonSerializer
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempestbot.interact.commands.eorzeadatabase.eorzeaDatabase
import cloud.drakon.tempestbot.interact.commands.lodestone.lodestoneHandler
import cloud.drakon.tempestbot.interact.commands.universalis
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Handler: RequestStreamHandler {
    companion object {
        val ktDiscord = KtDiscord(
            System.getenv("APPLICATION_ID"), System.getenv("BOT_TOKEN")
        ).Interaction(System.getenv("PUBLIC_KEY"))

        val ktorClient = HttpClient(Java)

        val json = Json {
            ignoreUnknownKeys =
                true // Not all fields returned by the Discord API are documented
            isLenient = true // TODO https://github.com/TempestProject/Tempest/issues/3
        }

        val mongoDatabase: MongoDatabase =
            MongoClients.create(System.getenv("MONGODB_URL"))
                .getDatabase(System.getenv("MONGODB_DATABASE"))
    }

    override fun handleRequest(
        inputStream: InputStream,
        outputStream: OutputStream,
        context: Context,
    ): Unit = runBlocking {
        val logger = context.logger

        val event: Interaction<*> = json.decodeFromString(
            InteractionJsonSerializer, inputStream.readAllBytes().decodeToString()
        )

        when (event.data) {
            is ApplicationCommandData -> {
                val applicationCommand = event as Interaction<ApplicationCommandData>

                when (event.type) {
                    2 -> when (applicationCommand.data !!.name) {
                        "eorzeadatabase" -> eorzeaDatabase(applicationCommand, logger)
                        "lodestone" -> lodestoneHandler(applicationCommand)
                        "universalis" -> universalis(applicationCommand, logger)
                        else -> logger.log("Unknown command: ${event.data !!.name}")
                    }

                    else -> logger.log("Unknown event type: ${event.type}")
                }
            }

            else -> logger.log("Unknown command type: ${event.javaClass}")
        }
    }
}
