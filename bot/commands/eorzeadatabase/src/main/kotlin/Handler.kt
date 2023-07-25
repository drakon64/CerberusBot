package cloud.drakon.dynamisbot.eorzeadatabase

import cloud.drakon.ktdiscord.KtDiscord
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
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

        val ktDiscord = KtDiscord(
            System.getenv("APPLICATION_ID"), System.getenv("BOT_TOKEN")
        ).Interaction(System.getenv("PUBLIC_KEY"))

        val ktXivApi = KtXivApi

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true // TODO https://github.com/TempestProject/Tempest/issues/3
        }

        val spanRegex = """<span.*?>|</span>""".toRegex()
        val newLineRegex = """\n{3,}""".toRegex()
    }

    override fun handleRequest(
        inputStream: InputStream,
        outputStream: OutputStream,
        context: Context,
    ) = runBlocking {
        val event: Interaction<ApplicationCommandData> =
            json.decodeFromString(inputStream.readAllBytes().decodeToString())

        eorzeaDatabase(event, context.logger)
    }
}
