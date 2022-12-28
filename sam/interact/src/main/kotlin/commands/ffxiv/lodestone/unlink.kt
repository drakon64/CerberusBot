package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import com.mongodb.client.model.Filters

suspend fun unlink(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member !!.user !!.id
    val guildId: String = event.guild_id !!

    mongoDatabase.getCollection("lodestone_link").deleteOne(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    )

    tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            "Removed Lodestone character link."
        ), event.token
    )
}

