package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.interact.Handler.Companion.tempestClient
import cloud.drakon.tempestbot.interact.api.XivApiClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun Link(
    event: Interaction<ApplicationCommandData>,
    characterName: String,
    world: String,
) {
    lateinit var userId: String
    val guildId: String = event.guild_id !!

    for (i in event.data !!.options !![0].options !!) {
        if (i.name == "user") {
            userId = i.value !!
        }
    }

    val characters = XivApiClient().characterSearch(
        characterName, world
    ).jsonObject["Results"] !!.jsonArray

    when (characters.size) {
        1 -> {
            mongoDatabase.getCollection("lodestone_link").findOneAndUpdate(
                Filters.and(
                    Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
                ), Updates.set(
                    "character_id", characters.jsonObject["ID"] !!.jsonPrimitive.int
                )
            )

            tempestClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    "Linked to character \"$characterName\" on \"$world\"."
                ), event.token
            )
        }

        0 -> tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "Could not find character \"$characterName\" on world \"$world\"."
            ), event.token
        )

        else -> tempestClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "Please specify the exact character name."
            ), event.token
        )
    }
}
