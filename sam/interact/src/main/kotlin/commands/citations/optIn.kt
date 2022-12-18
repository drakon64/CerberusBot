package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.MongoWriteException
import org.bson.Document

suspend fun optIn(event: Interaction<ApplicationCommandData>, guildId: String) {
    val document = Document()
    document.append("user_id", event.member !!.user !!.id)
    document.append("guild_id", guildId)

    val content: String = try {
        mongoCollection.insertOne(document)
        "Opted-in to citations!"
    } catch (e: MongoWriteException) {
        "Already opted-in to citations!"
    }

    Handler.tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = content
        ), interactionToken = event.token
    )
}
