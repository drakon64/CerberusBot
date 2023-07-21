package cloud.drakon.dynamisbot.lodestone

import cloud.drakon.dynamisbot.lodestone.Handler.Companion.ktDiscord
import cloud.drakon.dynamisbot.lodestone.Handler.Companion.mongoDatabase
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import com.mongodb.client.model.Filters

suspend fun unlink(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member!!.user!!.id
    val guildId: String = event.guildId!!
    var global = false

    for (i in event.data!!.options!![0].options!!) {
        when (i.name) {
            "global" -> global = i.value!!.toBoolean()
        }
    }

    if (global) {
        mongoDatabase.getCollection("lodestone_link")
            .deleteMany(Filters.eq("user_id", userId))
    } else {
        mongoDatabase.getCollection("lodestone_link").deleteOne(
            Filters.and(
                Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
            )
        )
    }

    ktDiscord.editOriginalInteractionResponse(
        EditWebhookMessage(
            "Removed Lodestone character link"
        ), event.token
    )
}
