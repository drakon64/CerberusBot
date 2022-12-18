package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import kotlinx.coroutines.delay
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun getCitation(
    event: Interaction<ApplicationCommandData>,
    userId: String,
    guildId: String,
) {
    val citations = mongoCollection.find(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    ).projection(
        Projections.fields(Projections.include("messages"), Projections.excludeId())
    ).first()

    if (citations != null) {
        val messagesJson =
            Handler.json.parseToJsonElement(citations.toJson()).jsonObject["messages"] !!.jsonArray
        val messages: MutableList<String> = emptyList<String>().toMutableList()

        if (messagesJson.isNotEmpty()) {
            for (i in messagesJson) {
                messages.add(i.jsonObject["content"] !!.jsonPrimitive.content)
            }

            Handler.tempestClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    content = "> " + messages.random()
                        .replace("\n", "\n>") + "\n- <@$userId>"
                ), interactionToken = event.token
            )
        } else {
            Handler.tempestClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    content = "No citations saved for the user!"
                ), interactionToken = event.token
            )

            delay(5000)

            Handler.tempestClient.deleteOriginalInteractionResponse(event.token)
        }
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
