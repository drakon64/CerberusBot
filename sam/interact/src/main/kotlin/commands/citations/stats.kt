package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import kotlinx.coroutines.delay

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
    ).projection(
        Projections.fields(Projections.include("messages"), Projections.excludeId())
    ).first()

    var error = true
    lateinit var citationCount: String
    lateinit var errorString: String

    if (citations != null) {
        val messagesJson: Citations =
            Handler.json.decodeFromString(Citations.serializer(), citations.toJson())

        if (messagesJson.messages != null) {
            error = false

            citationCount = messagesJson.messages.count().toString()
        } else {
            errorString = "No citations saved for the user!"
        }
    } else {
        errorString = "User has not opted-in to citations!"
    }

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        if (! error) {
            EditWebhookMessage(
                embeds = arrayOf(
                    Embed(
                        "Citation stats for user",
                        fields = arrayOf(EmbedField("Count", citationCount))
                    )
                )
            )
        } else {
            EditWebhookMessage(errorString)
        }, interactionToken = event.token
    )

    if (error) {
        delay(5000)

        Handler.ktDiscordClient.deleteOriginalInteractionResponse(event.token)
    }
}
