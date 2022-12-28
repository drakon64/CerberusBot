package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.tempest.File
import cloud.drakon.tempest.channel.Attachment
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import java.time.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bson.Document
import org.bson.types.Binary

suspend fun card(event: Interaction<ApplicationCommandData>) {
    lateinit var userId: String
    val guildId: String = event.guild_id !!

    for (i in event.data !!.options !![0].options !!) {
        when (i.name) {
            "user" -> userId = i.value !!
        }
    }

    var characterId: Int? = null
    val characterIdDocument = mongoDatabase.getCollection("lodestone_link").find(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    ).projection(
        Projections.fields(Projections.include("character_id"), Projections.excludeId())
    ).first()

    if (characterIdDocument != null) {
        characterId =
            Json.parseToJsonElement(characterIdDocument.toJson()).jsonObject["character_id"] !!.jsonPrimitive.int

        val mongoCollection = mongoDatabase.getCollection("lodestone")
        val mongoCard =
            mongoCollection.find(Filters.eq("character_id", characterId)).projection(
                Projections.fields(
                    Projections.include("card.binary"), Projections.excludeId()
                )
            ).first()

        lateinit var card: ByteArray
        if (mongoCard != null && mongoCard["card"] != null) {
            card = ((mongoCard["card"] as Document)["binary"] as Binary).data
        } else {
            card =
                HttpClient(Java).get("https://xiv-character-cards.drakon.cloud/characters/id/$characterId.png")
                    .body()

            val timestamp = LocalDateTime.now()
            mongoCollection.updateOne(
                Filters.eq("character_id", characterId), Updates.combine(
                    Updates.set(
                        "card.binary", card
                    ), Updates.set(
                        "card.timestamp", LocalDateTime.now()
                    ), Updates.set(
                        "timestamp", timestamp
                    )
                ), UpdateOptions().upsert(true)
            )
        }

        val filename = "${characterId}_card.png"

        tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                attachments = arrayOf(
                    Attachment(
                        id = "0", filename = filename
                    )
                ), files = arrayOf(
                    File(
                        id = "0",
                        filename = filename,
                        bytes = card,
                        contentType = "image/png"
                    )
                )
            ), event.token
        )
    } else {
        tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "User does not have a linked Lodestone character!"
            ), event.token
        )
    }
}
