package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.delay
import org.bson.Document

suspend fun addCitation(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
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

            logger.log("attachments: " + event.data !!.resolved !!.messages !!.values.first().attachments.toString())
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
        Handler.ktDiscordClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                content = "> " + message.replace("\n", "\n> ") + "\n- <@$userId>"
            ), interactionToken = event.token
        )
    } else {
        Handler.ktDiscordClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                content = "User has not opted-in to citations!"
            ), interactionToken = event.token
        )

        delay(5000)

        Handler.ktDiscordClient.deleteOriginalInteractionResponse(event.token)
    }
}
