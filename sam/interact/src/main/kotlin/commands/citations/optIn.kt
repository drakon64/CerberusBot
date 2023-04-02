package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscord
import com.mongodb.MongoWriteException
import org.bson.Document

suspend fun optIn(event: Interaction<ApplicationCommandData>) {
    val document = Document()
    document.append("user_id", event.member !!.user !!.id)
    document.append("guild_id", event.guildId)

    val content: String = try {
        mongoCollection.insertOne(document)
        "Opted-in to citations!"
    } catch (e: MongoWriteException) {
        "Already opted-in to citations!"
    }

    ktDiscord.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = content
        ), interactionToken = event.token
    )
}
