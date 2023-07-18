package cloud.drakon.dynamisbot.interact.commands.eorzeadatabase.item

import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun getStats(item: JsonObject, language: String) = coroutineScope {
    val stats = mutableListOf<String>()

    for (i in item["Stats"]!!.jsonObject.keys) {
        val key = Localisation.bonuses[i]?.getValue(language) ?: i

        val bonus = item["Stats"]!!.jsonObject[i]!!
        val value = bonus.jsonObject["NQ"]!!.jsonPrimitive.int
        val valueHq = bonus.jsonObject["HQ"]?.jsonPrimitive?.int

        if (valueHq != null) {
            stats.add(
                "$key +$value / +$valueHq <:hq:916051971063054406>"
            )
        } else {
            stats.add(
                "$key +$value"
            )
        }
    }

    return@coroutineScope stats.toList()
}
