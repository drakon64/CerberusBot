package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscordClient
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.tempestbot.interact.api.XivApiClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun link(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member !!.user !!.id
    val guildId: String = event.guildId !!
    lateinit var characterName: String
    lateinit var world: String

    for (i in event.data !!.options !![0].options !!) {
        when (i.name) {
            "character" -> characterName = i.value !!
            "world" -> world = i.value !!
        }
    }

    val characters = XivApiClient().characterSearch(
        characterName, world
    ).jsonObject["Results"] !!.jsonArray

    when (characters.size) {
        1 -> {
            mongoDatabase.getCollection("lodestone_link").updateOne(
                Filters.and(
                    Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)
                ), Updates.set(
                    "character_id", characters[0].jsonObject["ID"] !!.jsonPrimitive.int
                ), UpdateOptions().upsert(true)
            )

            ktDiscordClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    "Linked to character \"$characterName\" on \"$world\"."
                ), event.token
            )
        }

        0 -> ktDiscordClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "Could not find character \"$characterName\" on world \"$world\"."
            ), event.token
        )

        else -> ktDiscordClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                "Please specify the exact character name."
            ), event.token
        )
    }
}
