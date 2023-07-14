package cloud.drakon.cerberusbot.interact.commands.lodestone

import cloud.drakon.cerberusbot.interact.Handler.Companion.ktDiscord
import cloud.drakon.cerberusbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import com.mongodb.client.model.Filters

suspend fun unlink(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member!!.user!!.id
    val guildId: String = event.guildId!!

    mongoDatabase.getCollection("lodestone_link").deleteOne(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    )

    ktDiscord.editOriginalInteractionResponse(
        EditWebhookMessage(
            "Removed Lodestone character link."
        ), event.token
    )
}

