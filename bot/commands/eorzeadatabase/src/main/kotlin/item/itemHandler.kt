package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.cleanDescription
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun itemHandler(item: JsonObject, language: String, lodestone: String) =
    coroutineScope {
        return@coroutineScope when (item["ItemKind"]!!.jsonObject["ID"]!!.jsonPrimitive.int) {
            1, 2 -> { // Arms, Tools
                armsTools(item, language, lodestone)
            }

            3, 4 -> { // Armor, Accessories
                armorAccessories(item, language, lodestone)
            }

            5 -> { // Medicines & Meals
                medicineMeal(
                    item,
                    cleanDescription(item["Description"]!!.jsonPrimitive.content),
                    lodestone
                )
            }

            else -> {
                genericItem(item, lodestone)
            }
        }
    }
