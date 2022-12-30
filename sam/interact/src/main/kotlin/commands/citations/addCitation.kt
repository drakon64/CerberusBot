package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.discordkt.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.delay
import org.bson.Document

suspend fun addCitation(
    event: Interaction<ApplicationCommandData>,
) {
    lateinit var message: String
    lateinit var userId: String
    val guildId = event.guildId

    when (event.data !!.type) {
        1 -> for (i in event.data !!.options !![0].options !!) {
            when (i.name) {
                "citation" -> message = i.value !!
                "user" -> userId = i.value !!
            }
        }

        3 -> {
            message = event.data !!.resolved !!.messages !!.values.first().content
            userId = event.data !!.resolved !!.messages !!.values.first().author.id
        }
    }

    val document = Document()
    document.append("attachments", null)
    document.append("content", message)

    val query = mongoCollection.findOneAndUpdate(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        ), Updates.addToSet("messages", document)
    )

    if (query != null && query.isNotEmpty()) {
        Handler.tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                content = "> " + message.replace("\n", "\n> ") + "\n- <@$userId>"
            ), interactionToken = event.token
        )
    } else {
        Handler.tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                content = "User has not opted-in to citations!"
            ), interactionToken = event.token
        )

        delay(5000)

        Handler.tempestClient.deleteOriginalInteractionResponse(event.token)
    }
}
