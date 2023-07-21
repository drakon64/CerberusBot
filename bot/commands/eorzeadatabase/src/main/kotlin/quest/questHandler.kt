package cloud.drakon.dynamisbot.eorzeadatabase.quest

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedImage
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun questHandler(quest: JsonObject, lodestone: String) =
    coroutineScope {
        return@coroutineScope Embed(
            title = quest["Name"]!!.jsonPrimitive.content,
            description = quest["JournalGenre"]!!.jsonObject["Name"]!!.jsonPrimitive.content,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                quest["Name"]!!.jsonPrimitive.content.replace(
                    " ", "+"
                )
            }&db_search_category=quest",
            image = EmbedImage(url = "https://xivapi.com${quest["Banner"]!!.jsonPrimitive.content}"),
            thumbnail = EmbedThumbnail(url = quest["JournalGenre"]!!.jsonObject["IconHD"]!!.jsonPrimitive.content)
        )
    }
