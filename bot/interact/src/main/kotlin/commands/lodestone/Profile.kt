package cloud.drakon.cerberusbot.interact.commands.lodestone

import cloud.drakon.cerberusbot.interact.Handler.Companion.ktDiscord
import cloud.drakon.cerberusbot.interact.Handler.Companion.ktorClient
import cloud.drakon.cerberusbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktlodestone.KtLodestone
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import io.ktor.client.call.body
import io.ktor.client.request.get
import java.time.LocalDateTime
import org.bson.types.Binary

suspend fun profile(event: Interaction<ApplicationCommandData>) {
    lateinit var userId: String
    val guildId: String = event.guildId!!

    for (i in event.data!!.options!![0].options!!) {
        when (i.name) {
            "user" -> userId = i.value!!
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
        val characterId = characterIdDocument["character_id"] as Int

        val mongoCollection = mongoDatabase.getCollection("lodestone_profile")
        val mongoProfile =
            mongoCollection.find(Filters.eq("character_id", characterId)).projection(
                Projections.fields(
                    Projections.include("character_name"),
                    Projections.include("title"),
                    Projections.include("server"),
                    Projections.include("datacenter"),
                    Projections.include("avatar"),
                    Projections.include("class"),
                    Projections.include("level"),
                    Projections.excludeId()
                )
            ).first()

        val characterName: String
        val characterTitle: String?
        val characterServer: String
        val characterDatacenter: String
        val characterAvatar: ByteArray
        val characterClass: String
        val characterLevel: Int

        if (mongoProfile != null) {
            characterName = mongoProfile["character_name"] as String
            characterTitle = mongoProfile["title"] as String?
            characterServer = mongoProfile["server"] as String
            characterDatacenter = mongoProfile["datacenter"] as String
            characterAvatar = (mongoProfile["avatar"] as Binary).data
            characterClass = mongoProfile["class"] as String
            characterLevel = mongoProfile["level"] as Int
        } else {
            val profile = KtLodestone.getCharacter(characterId)

            characterName = profile.name
            characterTitle = profile.title
            characterServer = profile.server
            characterDatacenter = profile.dc
            characterAvatar = ktorClient.get(profile.avatar).body()
            characterClass = profile.activeClassJob.name
            characterLevel = profile.activeClassJob.level.toInt()

            mongoCollection.updateOne(
                Filters.eq("character_id", characterId), Updates.combine(
                    Updates.set("character_name", characterName),
                    Updates.set("title", characterTitle),
                    Updates.set("server", characterServer),
                    Updates.set("datacenter", characterDatacenter),
                    Updates.set("avatar", characterAvatar),
                    Updates.set("class", characterClass),
                    Updates.set("level", characterLevel),
                    Updates.set("timestamp", LocalDateTime.now())
                ), UpdateOptions().upsert(true)
            )
        }

        val filename = "${characterId}_avatar.jpg"

        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                embeds = arrayOf(
                    Embed(
                        title = characterName,
                        description = if (characterTitle != "null") { // TODO: Fix MongoDB returning nulls as a "null" string
                            "*$characterTitle*"
                        } else {
                            null
                        },
                        url = "https://eu.finalfantasyxiv.com/lodestone/character/$characterId/",
                        thumbnail = EmbedThumbnail(url = "attachment://${filename}"),
                        fields = arrayOf(
                            EmbedField(
                                name = "World [Datacenter]",
                                value = "$characterServer [$characterDatacenter]"
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
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                "User does not have a linked Lodestone character!"
            ), event.token
        )
    }
}
