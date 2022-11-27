package cloud.drakon.tempestbot.interact

import cloud.drakon.tempest.TempestClient
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class Handler: RequestStreamHandler {
    private val tempestClient = TempestClient(
        System.getenv("APPLICATION_ID"),
        System.getenv("BOT_TOKEN"),
        System.getenv("PUBLIC_KEY")
    )

    //    private val translateClient =
    //        TranslateClient { region = System.getenv("AWS_REGION") }

    override fun handleRequest(
        inputStream: InputStream,
        outputStream: OutputStream,
        context: Context,
    ): Unit = runBlocking {
        val event: Interaction<ApplicationCommandData> = Json {
            ignoreUnknownKeys = true
        }.decodeFromStream(inputStream)
        val logger = context.logger

        tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(content = "test"), event.token
        )
    }
}
