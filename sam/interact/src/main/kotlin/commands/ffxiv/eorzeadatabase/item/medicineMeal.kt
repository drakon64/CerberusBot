package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun medicineMeal(item: JsonObject, description: String, lodestone: String) =
    coroutineScope {
        val bonuses = mutableListOf<String>()

        val canBeHq = item["CanBeHq"] !!.jsonPrimitive.boolean

        for (i in item["Bonuses"] !!.jsonObject.keys) {
            val key = when (i) {
                "CriticalHit" -> "Critical Hit"
                "DirectHit" -> "Direct Hit"
                else -> i
            }

            val bonus = item["Bonuses"] !!.jsonObject[i] !!

            if (bonus.jsonObject["Relative"] !!.jsonPrimitive.boolean) {
                val value = bonus.jsonObject["Value"] !!.jsonPrimitive.int
                val max = bonus.jsonObject["Max"] !!.jsonPrimitive.int

                if (canBeHq) {
                    val valueHq = bonus.jsonObject["ValueHQ"] !!.jsonPrimitive.int
                    val maxHq = bonus.jsonObject["MaxHQ"] !!.jsonPrimitive.int

                    bonuses.add(
                        "$key +$value% (Max $max) / +$valueHq% (Max $maxHq) <:hq:916051971063054406>"
                    )
                } else {
                    bonuses.add(
                        "$key +$value% (Max $max)"
                    )
                }
            }
        }

        return@coroutineScope Embed(
            title = item["Name"] !!.jsonPrimitive.content,
            description = description,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
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
                ), EmbedField(
                    name = "Effects", value = bonuses.joinToString("\n"), inline = true
                )
            )
        )
    }
