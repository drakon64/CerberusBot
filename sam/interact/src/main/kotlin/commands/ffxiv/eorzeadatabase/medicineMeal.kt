package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun medicineMeal(item: JsonObject, description: String) = coroutineScope {
    val bonuses = mutableListOf<String>()

    for (i in item["Bonuses"] !!.jsonObject.keys) {
        val key = when (i) {
            "CriticalHit" -> "Critical Hit"
            "DirectHit" -> "Direct Hit"
            else -> i
        }

        val bonus = item["Bonuses"] !!.jsonObject[i] !!

        if (bonus.jsonObject["Relative"] !!.jsonPrimitive.boolean) {
            bonuses.add("$key +${bonus.jsonObject["Value"] !!.jsonPrimitive.content}% (Max ${bonus.jsonObject["Max"] !!.jsonPrimitive.content})")
        }
    }

    val bonusesString = bonuses.joinToString("\n")

    return@coroutineScope Embed(
        title = item["Name"] !!.jsonPrimitive.content,
        description = description,
        url = "https://ffxiv.gamerescape.com/wiki/${
            item["Name"] !!.jsonPrimitive.content.replace(
                " ", "_"
            )
        }",
        thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"] !!.jsonPrimitive.content}"),
        fields = arrayOf(
            EmbedField(
                name = "Item Level",
                value = item["LevelItem"] !!.jsonPrimitive.content,
                inline = true
            ), EmbedField(name = "Effects", value = bonusesString, inline = true)
        )
    )
}
