package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.json
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import kotlinx.coroutines.delay
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
    suspend fun citationHandler() {
        lateinit var userId: String
        val guildId: String = event.guild_id !!

        when (event.data !!.type) {
            1 -> {
                val options = event.data !!.options !![0]

                for (i in options.options !!) {
                    if (i.name == "user") {
                        userId = i.value !!
                    }
                }

                if (options.name == "get") {
                    return getCitation(userId, guildId)
                }
            }

            2 -> {
                userId = event.data !!.resolved !!.users !!.keys.first()

                return getCitation(userId, guildId)
            }
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
            val messagesJson =
                json.parseToJsonElement(citations.toJson()).jsonObject["messages"] !!.jsonArray
            val messages: MutableList<String> = emptyList<String>().toMutableList()

            if (messagesJson.isNotEmpty()) {
                for (i in messagesJson) {
                    messages.add(i.jsonObject["content"] !!.jsonPrimitive.content)
                }

                tempestClient.editOriginalInteractionResponse(
                    EditWebhookMessage(
                        content = "> " + messages.random()
                            .replace("\n", "\n>") + "\n- <@$userId>"
                    ), interactionToken = event.token
                )
            } else {
                tempestClient.editOriginalInteractionResponse(
                    EditWebhookMessage(
                        content = "No citations saved for the user!"
                    ), interactionToken = event.token
                )

                delay(5000)

                tempestClient.deleteOriginalInteractionResponse(event.token)
            }
        } else {
            tempestClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    content = "User has not opted-in to citations!"
                ), interactionToken = event.token
            )

            delay(5000)

            tempestClient.deleteOriginalInteractionResponse(event.token)
        }
    }
}
