package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.discordkt.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.MongoWriteException
import org.bson.Document

suspend fun optIn(event: Interaction<ApplicationCommandData>) {
    val document = Document()
    document.append("user_id", event.member !!.user !!.id)
    document.append("guild_id", event.guild_id)

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
