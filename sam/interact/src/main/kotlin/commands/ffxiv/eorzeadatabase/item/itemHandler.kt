package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.item

import cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.cleanDescription
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun itemHandler(item: JsonObject, language: String? = null) = coroutineScope {
    return@coroutineScope when (item["ItemKind"] !!.jsonObject["ID"] !!.jsonPrimitive.int) {
        1 -> { // Arms
            arms(item, language)
        }

        5 -> { // Medicines & Meals
            medicineMeal(
                item, cleanDescription(item["Description"] !!.jsonPrimitive.content)
            )
        }

        else -> {
            genericItem(item)
        }
    }
}
