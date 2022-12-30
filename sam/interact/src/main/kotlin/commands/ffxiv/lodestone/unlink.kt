package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.discordkt.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.discordKtClient
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import com.mongodb.client.model.Filters

suspend fun unlink(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member !!.user !!.id
    val guildId: String = event.guildId !!

    mongoDatabase.getCollection("lodestone_link").deleteOne(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    )

    discordKtClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            "Removed Lodestone character link."
        ), event.token
    )
}

