package cloud.drakon.cerberusbot.interact.commands.eorzeadatabase.item

import cloud.drakon.cerberusbot.interact.commands.eorzeadatabase.cleanDescription
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun genericItem(item: JsonObject, lodestone: String) = coroutineScope {
    val descriptionElement = item["Description"]?.jsonPrimitive?.content
    val description = if (descriptionElement != null) {
        cleanDescription(descriptionElement)
    } else {
        null
    }

    return@coroutineScope Embed(
        title = item["Name"]!!.jsonPrimitive.content,
        description = description,
        url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
            item["Name"]!!.jsonPrimitive.content.replace(
                " ", "+"
            )
        }",
        thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"]!!.jsonPrimitive.content}")
    )
}
