package cloud.drakon.dynamisbot.interact.commands.lodestone

import cloud.drakon.dynamisbot.interact.Handler.Companion.ktDiscord
import cloud.drakon.dynamisbot.interact.Handler.Companion.mongoDatabase
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktlodestone.KtLodestone
import cloud.drakon.ktlodestone.search.World
import cloud.drakon.ktlodestone.search.result.CharacterSearchResult
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates

lateinit var character: CharacterSearchResult

suspend fun link(event: Interaction<ApplicationCommandData>) {
    val userId: String = event.member!!.user!!.id
    val guildId: String = event.guildId!!
    lateinit var characterName: String
    lateinit var world: String

    for (i in event.data!!.options!![0].options!!) {
        when (i.name) {
            "character" -> characterName = i.value!!
            "world" -> world = i.value!!
        }
    }

    val characters = KtLodestone.searchCharacter(
        characterName, World.valueOf(world)
    )

    suspend fun couldNotFindCharacter() {
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                "Could not find character \"$characterName\" on world \"$world\"."
            ), event.token
        )
    }

    if (characters != null) {
        for (i in characters) {
            if (i.name == characterName) {
                character = i
            }
        }

        if (!::character.isInitialized) {
            couldNotFindCharacter()
        } else {
            mongoDatabase.getCollection("lodestone_link").updateOne(
                Filters.and(
                    Filters.eq("user_id", userId),
                    Filters.eq("guild_id", guildId)
                ),
                Updates.set("character_id", character.id),
                UpdateOptions().upsert(true)
            )

            ktDiscord.editOriginalInteractionResponse(
                EditWebhookMessage(
                    "Linked to character \"$characterName\" on \"$world\"."
                ), event.token
            )
        }
    } else {
        couldNotFindCharacter()
    }
}
