package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.item.Localisation
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun arms(item: JsonObject, searchLanguage: String? = null) = coroutineScope {
    val language = searchLanguage ?: "en"
    val bonuses = mutableListOf<String>()

    for (i in item["Stats"] !!.jsonObject.keys) {
        val key = Localisation.bonuses.getValue(i).getValue(language)

        val bonus = item["Stats"] !!.jsonObject[i] !!
        val value = bonus.jsonObject["NQ"] !!.jsonPrimitive.int
        val valueHq = bonus.jsonObject["HQ"]?.jsonPrimitive?.int

        if (valueHq != null) {
            bonuses.add(
                "$key +$value / +$valueHq <:hq:916051971063054406>"
            )
        } else {
            bonuses.add(
                "$key +$value"
            )
        }
    }

    val damageType =
        when (item["ClassJobUse"] !!.jsonObject["ClassJobCategory"] !!.jsonObject["ID"] !!.jsonPrimitive.int) {
            30 -> Localisation.damageType.getValue("Physical Damage").getValue(language)
            31 -> Localisation.damageType.getValue("Magic Damage").getValue(language)
            else -> throw Throwable("Unknown class/job category: $this")
        }

    val delay = ((item["DelayMs"] !!.jsonPrimitive.int).toDouble() / 1000).toString()
    val classJob = """
        ${item["ClassJobCategory"] !!.jsonObject["Name"] !!.jsonPrimitive.content}
        ${Localisation.level.getValue(language)} ${item["LevelEquip"] !!.jsonPrimitive.content}
    """.trimIndent()

    return@coroutineScope Embed(
        title = item["Name"] !!.jsonPrimitive.content,
        description = item["ItemUICategory"] !!.jsonObject["Name"] !!.jsonPrimitive.content,
        url = "https://ffxiv.gamerescape.com/wiki/${
            item["Name"] !!.jsonPrimitive.content.replace(
                " ", "_"
            )
        }",
        thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"] !!.jsonPrimitive.content}"),
        fields = arrayOf(
            EmbedField(
                name = Localisation.itemLevel.getValue(language),
                value = item["LevelItem"] !!.jsonPrimitive.content,
            ), EmbedField(
                name = damageType,
                value = item["DamagePhys"] !!.jsonPrimitive.content,
                inline = true
            ), EmbedField(
                name = Localisation.autoAttack.getValue(language),
                value = "",
                inline = true
            ), EmbedField(
                name = Localisation.delay.getValue(language),
                value = delay,
                inline = true
            ), EmbedField(
                name = "Class/Job", value = classJob, inline = true
            ), EmbedField(
                name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                value = bonuses.joinToString("\n"),
                inline = true
            )
        )
    )
}
