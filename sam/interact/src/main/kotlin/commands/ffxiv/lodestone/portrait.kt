package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.tempest.File
import cloud.drakon.tempest.channel.Attachment
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import cloud.drakon.tempestbot.interact.api.XivApiClient
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

suspend fun portrait(event: Interaction<ApplicationCommandData>) {
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
        val mongoPortrait =
            mongoCollection.find(Filters.eq("character_id", characterId)).projection(
                Projections.fields(
                    Projections.include("portrait.binary"), Projections.excludeId()
                )
            ).first()

        lateinit var portrait: ByteArray
        if (mongoPortrait != null && mongoPortrait["portrait"] != null) {
            portrait =
                ((mongoPortrait["portrait"] as Document)["binary"] as Binary).data
        } else {
            val ktorClient = HttpClient(Java)
            portrait = ktorClient.get(
                XivApiClient(ktorClient = ktorClient).profile(characterId).jsonObject["Character"] !!.jsonObject["Portrait"] !!.jsonPrimitive.content
            ).body()

            val timestamp = LocalDateTime.now()
            mongoCollection.updateOne(
                Filters.eq("character_id", characterId), Updates.combine(
                    Updates.set(
                        "portrait.binary", portrait
                    ), Updates.set(
                        "portrait.timestamp", timestamp
                    ), Updates.set(
                        "timestamp", timestamp
                    )
                ), UpdateOptions().upsert(true)
            )
        }

        val filename = "${characterId}_portrait.jpg"

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
                        bytes = portrait,
                        contentType = "image/jpeg"
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
