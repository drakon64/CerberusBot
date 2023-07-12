package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.cleanDescription
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun genericItem(item: JsonObject) = coroutineScope {
    val descriptionElement = item["Description"]?.jsonPrimitive?.content
    val description = if (descriptionElement != null) {
        cleanDescription(descriptionElement)
    } else {
        null
    }

    return@coroutineScope Embed(
        title = item["Name"] !!.jsonPrimitive.content,
        description = description,
        url = "https://ffxiv.gamerescape.com/wiki/${
            item["Name"] !!.jsonPrimitive.content.replace(
                " ", "_"
            )
        }",
        thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"] !!.jsonPrimitive.content}")
    )
}
