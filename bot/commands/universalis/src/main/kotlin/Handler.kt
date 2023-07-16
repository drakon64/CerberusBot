package cloud.drakon.dynamisbot.universalis

import cloud.drakon.ktdiscord.KtDiscord
import cloud.drakon.ktdiscord.channel.message.Message
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktuniversalis.KtUniversalis
import cloud.drakon.ktxivapi.KtXivApi
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import kotlinx.coroutines.runBlocking

class Handler: RequestHandler<Interaction<ApplicationCommandData>, Message> {
    companion object {
        // Initialise these during the initialization phase

        val ktDiscord = KtDiscord(
            System.getenv("APPLICATION_ID"), System.getenv("BOT_TOKEN")
        ).Interaction(System.getenv("PUBLIC_KEY"))

        val ktXivApi = KtXivApi
        val ktUniversalis = KtUniversalis
    }

    override fun handleRequest(
        input: Interaction<ApplicationCommandData>,
        context: Context,
    ) = runBlocking {
        return@runBlocking Universalis.universalisCommand(input, context.logger)
    }
}
