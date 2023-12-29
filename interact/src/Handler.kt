package cloud.drakon.dynamisbot.interact

import cloud.drakon.dynamisbot.discord.DiscordInteractionsClient
import cloud.drakon.dynamisbot.discord.interaction.Interaction
import cloud.drakon.dynamisbot.discord.webhook.EditWebhookMessage
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import kotlinx.coroutines.runBlocking

class Handler : RequestHandler<Interaction, Unit> {
    private val discordInteractionsClient = DiscordInteractionsClient(System.getenv("DISCORD_APPLICATION_ID").toLong())

    override fun handleRequest(input: Interaction, context: Context) {
        runBlocking {
            discordInteractionsClient.editOriginalInteractionResponse(input.token, EditWebhookMessage("Hello World!"))
        }
    }
}
