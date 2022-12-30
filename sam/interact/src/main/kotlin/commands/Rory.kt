package cloud.drakon.tempestbot.interact.commands

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.rory.RoryClient

suspend fun rory(event: Interaction<ApplicationCommandData>) {
    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            RoryClient().getRory().url
        ), event.token
    )
}
