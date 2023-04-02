package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.json
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscord
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import kotlinx.coroutines.delay
import org.bson.types.ObjectId

suspend fun stats(event: Interaction<ApplicationCommandData>) {
    lateinit var userId: String

    when (event.data !!.type) {
        1 -> for (i in event.data !!.options !![0].options !!) {
            when (i.name) {
                "user" -> userId = i.value !!
            }
        }

        2 -> userId = event.data !!.resolved !!.users !!.keys.first()
    }

    val citations = mongoCollection.find(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", event.guildId)
        )
    ).projection(Projections.include("messages")).first()

    var error = true
    lateinit var citationCount: String
    lateinit var since: String
    lateinit var errorString: String

    if (citations != null) {
        val messagesJson: Citations =
            json.decodeFromString(Citations.serializer(), citations.toJson())

        if (messagesJson.messages != null) {
            error = false

            citationCount = messagesJson.messages.count().toString()
            since = (citations["_id"] as ObjectId).timestamp.toString()
        } else {
            errorString = "No citations saved for the user!"
        }
    } else {
        errorString = "User has not opted-in to citations!"
    }

    ktDiscord.editOriginalInteractionResponse(
        if (! error) {
            EditWebhookMessage(
                embeds = arrayOf(
                    Embed(
                        "Citation stats", fields = arrayOf(
                            EmbedField("User", "<@${userId}>"),
                            EmbedField("Count", citationCount, inline = true),
                            EmbedField("Since", "<t:${since}:D>", inline = true)
                        )
                    )
                )
            )
        } else {
            EditWebhookMessage(errorString)
        }, interactionToken = event.token
    )

    if (error) {
        delay(5000)

        ktDiscord.deleteOriginalInteractionResponse(event.token)
    }
}
