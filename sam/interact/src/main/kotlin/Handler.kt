package cloud.drakon.tempestbot.interact

import cloud.drakon.tempest.TempestClient
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.InteractionJsonSerializer
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempestbot.interact.commands.ffxiv.universalis
import cloud.drakon.tempestbot.interact.commands.translate
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
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
    }

    private val json = Json

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
