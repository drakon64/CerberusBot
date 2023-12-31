package cloud.drakon.dynamisbot.interact

import cloud.drakon.ktdiscord.KtDiscord
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import kotlinx.coroutines.runBlocking

class Handler : RequestHandler<Interaction, Unit> {
    private val discordInteractionsClient = KtDiscord

    override fun handleRequest(input: Interaction, context: Context) {
        runBlocking {
            discordInteractionsClient.editOriginalInteractionResponse(input.token, EditWebhookMessage("Hello World!"))
        }
    }
}
