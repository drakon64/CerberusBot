package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscordClient
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

    ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            "Removed Lodestone character link."
        ), event.token
    )
}

