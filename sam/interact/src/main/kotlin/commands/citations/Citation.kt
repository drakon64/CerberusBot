package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.channel.message.MessageFlags
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.interaction.response.InteractionCallbackType
import cloud.drakon.tempest.interaction.response.InteractionResponse
import cloud.drakon.tempest.interaction.response.interactioncallbackdata.MessageCallbackData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.json
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import cloud.drakon.tempestbot.interact.mongoDatabase
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bson.Document

val mongoCollection: MongoCollection<Document> =
    mongoDatabase.getCollection("citations")

class Citation(
    private val event: Interaction<ApplicationCommandData>,
    private val logger: LambdaLogger,
) {
    suspend fun citation() {
        val options = event.data !!.options !![0]
        lateinit var userId: String
        val guildId: String = event.guild_id !!

        for (i in options.options !!) {
            if (i.name == "user") {
                userId = i.value !!
            }
        }

        if (options.name == "get") {
            return getCitation(userId, guildId)
        }
    }

    private suspend fun getCitation(userId: String, guildId: String) {
        val citations = mongoCollection.find(
            Filters.and(
                eq("user_id", userId), eq("guild_id", guildId)
            )
        ).projection(
            Projections.fields(Projections.include("messages"), Projections.excludeId())
        ).first()

        if (citations != null) {
            val citationsJson = json.parseToJsonElement(citations.toJson())
            val messagesJson = citationsJson.jsonObject["messages"]
            lateinit var messages: MutableList<String>

            if (messagesJson != null) {
                for (i in messagesJson.jsonArray) {
                    messages.add(i.jsonObject["content"] !!.jsonPrimitive.content)
                }

                tempestClient.editOriginalInteractionResponse(
                    EditWebhookMessage(content = messages.random()),
                    interactionToken = event.token
                )
            }
        } else {
            tempestClient.createInteractionResponse(
                InteractionResponse(
                    type = InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE,
                    data = MessageCallbackData(
                        content = "No citations saved for the user!",
                        flags = MessageFlags.EPHEMERAL
                    )
                ), interactionId = event.id, interactionToken = event.token
            )
        }
    }
}
