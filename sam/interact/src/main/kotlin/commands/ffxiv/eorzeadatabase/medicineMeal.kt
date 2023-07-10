package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedAuthor
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun medicineMeal(item: JsonObject) = coroutineScope {
    val bonuses = mutableListOf<String>()

    for (i in item["Bonuses"] !!.jsonObject.keys) {
        val bonus = item["Bonuses"] !!.jsonObject[i] !!

        if (bonus.jsonObject["Relative"] !!.jsonPrimitive.boolean) {
            bonuses.add("$i +${bonus.jsonObject["Value"] !!.jsonPrimitive.content} (Max ${bonus.jsonObject["Max"] !!.jsonPrimitive.content})")
        }
    }

    val bonusesString = bonuses.joinToString("\n")

    return@coroutineScope Embed(
        title = item["Name"] !!.jsonPrimitive.content,
        description = item["Description"] !!.jsonPrimitive.content,
        url = "https://ffxiv.gamerescape.com/wiki/${
            item["Name"] !!.jsonPrimitive.content.replace(
                " ", "_"
            )
        }",
        thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"] !!.jsonPrimitive.content}"),
        author = EmbedAuthor(
            "TempestBot", "https://github.com/TempestProject/TempestBot"
        ),
        fields = arrayOf(
            EmbedField(
                name = "Item Level", value = item["LevelItem"] !!.jsonPrimitive.content
            ), EmbedField(name = "Effects", value = bonusesString)
        )
    )
}
