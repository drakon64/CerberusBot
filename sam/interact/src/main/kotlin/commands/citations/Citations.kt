package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.channel.message.MessageFlags
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.interaction.response.InteractionCallbackType
import cloud.drakon.tempest.interaction.response.InteractionResponse
import cloud.drakon.tempest.interaction.response.interactioncallbackdata.MessageCallbackData
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import cloud.drakon.tempestbot.interact.mongoDatabase
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import kotlinx.serialization.json.Json
import org.bson.Document

val mongoCollection: MongoCollection<Document> =
    mongoDatabase.getCollection("citations")

class Citations(
    private val userId: String,
    private val guildId: String,
    private val event: Interaction<ApplicationCommandData>,
) {
    suspend fun getCitation() {
        val citations = mongoCollection.find(
            Filters.and(
                eq("user_id", userId), eq("guild_id", guildId)
            )
        ).projection(
            Projections.fields(Projections.include("messages"), Projections.excludeId())
        ).first()

        if (citations != null) {
            Json.parseToJsonElement(citations.toJson())
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
