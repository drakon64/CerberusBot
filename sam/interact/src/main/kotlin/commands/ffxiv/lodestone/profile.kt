package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.discordkt.channel.Attachment
import cloud.drakon.discordkt.channel.embed.Embed
import cloud.drakon.discordkt.channel.embed.EmbedField
import cloud.drakon.discordkt.channel.embed.EmbedThumbnail
import cloud.drakon.discordkt.file.File
import cloud.drakon.discordkt.interaction.Interaction
import cloud.drakon.discordkt.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.discordkt.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.discordKtClient
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
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
import kotlin.properties.Delegates
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bson.Document
import org.bson.types.Binary

suspend fun profile(event: Interaction<ApplicationCommandData>) {
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

        val mongoCollection = mongoDatabase.getCollection("lodestone")
        val mongoProfile =
            mongoCollection.find(Filters.eq("character_id", characterId)).projection(
                Projections.fields(
                    Projections.include("profile.character_name"),
                    Projections.include("profile.title"),
                    Projections.include("profile.server"),
                    Projections.include("profile.datacenter"),
                    Projections.include("profile.avatar"),
                    Projections.include("profile.class"),
                    Projections.include("profile.level"),
                    Projections.excludeId()
                )
            ).first()

        lateinit var characterName: String
        lateinit var characterTitle: String
        lateinit var characterServer: String
        lateinit var characterDatacenter: String
        lateinit var characterAvatar: ByteArray
        lateinit var characterClass: String
        var characterLevel by Delegates.notNull<Int>()

        if (mongoProfile != null && mongoProfile["profile"] != null) {
            val profile = mongoProfile["profile"] as Document

            characterName = profile["character_name"] as String
            characterTitle = profile["title"] as String
            characterServer = profile["server"] as String
            characterDatacenter = profile["datacenter"] as String
            characterAvatar = (profile["avatar"] as Binary).data
            characterClass = profile["class"] as String
            characterLevel = profile["level"] as Int
        } else {
            val ktorClient = HttpClient(Java)
            val profile = XivApiClient(ktorClient = ktorClient).profile(
                characterId, true
            ).jsonObject["Character"] !!

            characterName = profile.jsonObject["Name"] !!.jsonPrimitive.content
            characterTitle =
                profile.jsonObject["Title"] !!.jsonObject["Name"] !!.jsonPrimitive.content
            characterServer = profile.jsonObject["Server"] !!.jsonPrimitive.content
            characterDatacenter = profile.jsonObject["DC"] !!.jsonPrimitive.content
            characterAvatar =
                ktorClient.get(profile.jsonObject["Avatar"] !!.jsonPrimitive.content)
                    .body()
            characterClass =
                profile.jsonObject["ActiveClassJob"] !!.jsonObject["UnlockedState"] !!.jsonObject["Name"] !!.jsonPrimitive.content
            characterLevel =
                profile.jsonObject["ActiveClassJob"] !!.jsonObject["Level"] !!.jsonPrimitive.content.toInt()

            val timestamp = LocalDateTime.now()
            mongoCollection.updateOne(
                Filters.eq("character_id", characterId), Updates.combine(
                    Updates.set("profile.character_name", characterName),
                    Updates.set("profile.title", characterTitle),
                    Updates.set("profile.server", characterServer),
                    Updates.set("profile.datacenter", characterDatacenter),
                    Updates.set("profile.avatar", characterAvatar),
                    Updates.set("profile.class", characterClass),
                    Updates.set("profile.level", characterLevel),
                    Updates.set("profile.timestamp", timestamp),
                    Updates.set("timestamp", timestamp)
                ), UpdateOptions().upsert(true)
            )
        }

        val filename = "${characterId}_avatar.jpg"

        discordKtClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                embeds = arrayOf(
                    Embed(
                        title = characterName,
                        description = "*$characterTitle*",
                        url = "https://eu.finalfantasyxiv.com/lodestone/character/$characterId",
                        thumbnail = EmbedThumbnail(url = "attachment://${filename}"),
                        fields = arrayOf(
                            EmbedField(
                                name = "World (Datacenter)",
                                value = "$characterServer ($characterDatacenter)"
                            ),
                            EmbedField(name = "Class/Job", value = characterClass),
                            EmbedField(
                                name = "Level", value = characterLevel.toString()
                            )
                        )
                    )
                ), files = arrayOf(
                    File(
                        id = "0",
                        filename = filename,
                        contentType = "image/jpeg",
                        bytes = characterAvatar
                    )
                ), attachments = arrayOf(
                    Attachment(
                        id = "0", filename = filename, description = characterName
                    )
                )
            ), event.token
        )
    } else {
        discordKtClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "User does not have a linked Lodestone character!"
            ), event.token
        )
    }
}
