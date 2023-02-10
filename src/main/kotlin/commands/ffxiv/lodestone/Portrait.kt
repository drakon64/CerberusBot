package cloud.drakon.tempestbot.commands.ffxiv.lodestone

import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.Handler.Companion.ktDiscordClient
import cloud.drakon.tempestbot.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.api.xivapi.XivApiClient
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
import org.bson.types.Binary

suspend fun portrait(event: Interaction<ApplicationCommandData>) {
    lateinit var userId: String
    val guildId: String = event.guildId !!

    for (i in event.data !!.options !![0].options !!) {
        when (i.name) {
            "user" -> userId = i.value !!
        }
    }

    val characterIdDocument = mongoDatabase.getCollection("lodestone_link").find(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
        )
    ).projection(
        Projections.fields(Projections.include("character_id"), Projections.excludeId())
    ).first()

    if (characterIdDocument != null) {
        val characterId =
            Json.parseToJsonElement(characterIdDocument.toJson()).jsonObject["character_id"] !!.jsonPrimitive.int

        val mongoCollection = mongoDatabase.getCollection("lodestone_portrait")
        val mongoPortrait =
            mongoCollection.find(Filters.eq("character_id", characterId)).projection(
                Projections.fields(
                    Projections.include("binary"), Projections.excludeId()
                )
            ).first()

        val portrait: ByteArray
        if (mongoPortrait != null) {
            portrait = (mongoPortrait["binary"] as Binary).data
        } else {
            val ktorClient = HttpClient(Java)
            portrait = ktorClient.get(
                XivApiClient(ktorClient = ktorClient).profile(characterId).jsonObject["Character"] !!.jsonObject["Portrait"] !!.jsonPrimitive.content
            ).body()

            mongoCollection.updateOne(
                Filters.eq("character_id", characterId), Updates.combine(
                    Updates.set("binary", portrait),
                    Updates.set("timestamp", LocalDateTime.now())
                ), UpdateOptions().upsert(true)
            )
        }

        val filename = "${characterId}_portrait.jpg"

        ktDiscordClient.editOriginalInteractionResponse(
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
        ktDiscordClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "User does not have a linked Lodestone character!"
            ), event.token
        )
    }
}
