package cloud.drakon.tempestbot.interact.commands

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.rory.Rory
import cloud.drakon.tempestbot.interact.api.rory.RoryClient

suspend fun rory(event: Interaction<ApplicationCommandData>) {
    var id: Byte? = null

    for (i in event.data !!.options !![0].options !!) {
        when (i.name) {
            "id" -> id = i.value !!.toByte()
        }
    }

    val rory: Rory = if (id != null) {
        Rory(id)
    } else {
        RoryClient().getRory()
    }

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(content = "https://rory.cat/id/${rory.id}"), event.token
    )
}
