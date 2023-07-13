package cloud.drakon.tempestbot.interact.commands.lodestone

import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscord
import cloud.drakon.tempestbot.interact.Handler.Companion.ktorClient
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import io.ktor.client.call.body
import io.ktor.client.request.get
import java.time.LocalDateTime
import org.bson.types.Binary

suspend fun card(event: Interaction<ApplicationCommandData>) {
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
        val characterId = characterIdDocument["character_id"] as Int

        val mongoCollection = mongoDatabase.getCollection("lodestone_card")
        val mongoCard =
            mongoCollection.find(Filters.eq("character_id", characterId)).projection(
                Projections.fields(
                    Projections.include("binary"), Projections.excludeId()
                )
            ).first()

        val card: ByteArray
        if (mongoCard != null) {
            card = (mongoCard["binary"] as Binary).data
        } else {
            card =
                ktorClient.get("https://xiv-character-cards.drakon.cloud/characters/id/$characterId.png")
                    .body()

            mongoCollection.updateOne(
                Filters.eq("character_id", characterId), Updates.combine(
                    Updates.set("binary", card),
                    Updates.set("timestamp", LocalDateTime.now())
                ), UpdateOptions().upsert(true)
            )
        }

        val filename = "${characterId}_card.png"

        ktDiscord.editOriginalInteractionResponse(
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
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                "User does not have a linked Lodestone character!"
            ), event.token
        )
    }
}
