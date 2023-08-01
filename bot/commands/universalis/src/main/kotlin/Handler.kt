package cloud.drakon.dynamisbot.universalis

import cloud.drakon.ktdiscord.KtDiscord
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.interactiondata.ApplicationCommandData
import cloud.drakon.ktuniversalis.KtUniversalis
import cloud.drakon.ktxivapi.KtXivApi
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Handler: RequestStreamHandler {
    companion object {
        // Initialise these during the initialization phase

        val json = Json {
            ignoreUnknownKeys =
                true // Not all fields returned by the Discord API are documented
            isLenient = true // TODO https://github.com/TempestProject/Tempest/issues/3
        }

        val ktDiscord = KtDiscord(
            System.getenv("APPLICATION_ID"), System.getenv("BOT_TOKEN")
        ).Interaction(System.getenv("PUBLIC_KEY"))

        val ktXivApi = KtXivApi
        val ktUniversalis = KtUniversalis

        val spanRegex = """<span.*?>|</span>""".toRegex()
        val newLineRegex = """\n{3,}""".toRegex()
    }

    override fun handleRequest(
        inputStream: InputStream,
        outputStream: OutputStream,
        context: Context,
    ): Unit = runBlocking {
        val event: Interaction<ApplicationCommandData> =
            json.decodeFromString(inputStream.readAllBytes().decodeToString())

        universalisCommand(event, context.logger)
    }
}
