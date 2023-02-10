package cloud.drakon.tempestbot.commands

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.Handler
import cloud.drakon.tempestbot.api.rory.Rory
import cloud.drakon.tempestbot.api.rory.RoryClient

suspend fun rory(event: Interaction<ApplicationCommandData>) {
    var id: Byte? = null

    if (event.data !!.options != null) {
        for (i in event.data !!.options !!) {
            when (i.name) {
                "id" -> id = i.value !!.toByte()
            }
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
