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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun portrait(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member !!.user !!.id
    val guildId: String = event.guild_id !!

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

        val ktorClient = HttpClient(Java)
        val portrait: ByteArray = ktorClient.get(
            XivApiClient(ktorClient = ktorClient).profile(characterId).jsonObject["Character"] !!.jsonObject["Portrait"] !!.jsonPrimitive.content
        ).body()

        mongoDatabase.getCollection("lodestone").updateOne(
            Filters.eq("character_id", characterId), Updates.set(
                "portrait", portrait
            ), UpdateOptions().upsert(true)
        )

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
