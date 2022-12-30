package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.discordkt.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import kotlinx.coroutines.delay
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun getCitation(event: Interaction<ApplicationCommandData>) {
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
    lateinit var content: String

    if (citations != null) {
        val messagesJson =
            Handler.json.parseToJsonElement(citations.toJson()).jsonObject["messages"] !!.jsonArray
        val messages: MutableList<String> = emptyList<String>().toMutableList()

        if (messagesJson.isNotEmpty()) {
            for (i in messagesJson) {
                messages.add(i.jsonObject["content"] !!.jsonPrimitive.content)
            }

            error = false
            content = "> " + messages.random().replace("\n", "\n> ") + "\n- <@$userId>"
        } else {
            content = "No citations saved for the user!"
        }
    } else {
        content = "User has not opted-in to citations!"
    }

    Handler.discordKtClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = content
        ), interactionToken = event.token
    )

    if (error) {
        delay(5000)

        Handler.discordKtClient.deleteOriginalInteractionResponse(event.token)
    }
}
