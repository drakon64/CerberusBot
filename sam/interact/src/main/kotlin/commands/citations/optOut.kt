package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.discordkt.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import org.bson.Document

suspend fun optOut(event: Interaction<ApplicationCommandData>) {
    val userId = event.member !!.user !!.id
    val guildId = event.guildId

    val document = Document()
    document.append("user_id", userId)
    document.append("guild_id", guildId)

    mongoCollection.deleteOne(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    )

    Handler.tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = "Opted out of citations!"
        ), interactionToken = event.token
    )
}
