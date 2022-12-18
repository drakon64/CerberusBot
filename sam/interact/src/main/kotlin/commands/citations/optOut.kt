package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import org.bson.Document

suspend fun optOut(event: Interaction<ApplicationCommandData>, guildId: String) {
    val document = Document()
    document.append("user_id", event.member !!.user !!.id)
    document.append("guild_id", guildId)

    mongoCollection.deleteOne(
        Filters.and(
            Filters.eq("user_id", event.member !!.user !!.id),
            Filters.eq("guild_id", guildId)
        )
    )

    Handler.tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = "Opted out of citations!"
        ), interactionToken = event.token
    )
}
