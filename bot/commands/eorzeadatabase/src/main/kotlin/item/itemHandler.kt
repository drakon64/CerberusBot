package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun itemHandler(item: JsonObject, language: String, lodestone: String) =
    when (item["ItemKind"]!!.jsonObject["ID"]!!.jsonPrimitive.int) {
        1, 2 -> json.decodeFromJsonElement<ArmsTools>(item)

        3 -> {
            if (item["EquipSlotCategoryTargetID"]!!.jsonPrimitive.int == 2) {
                json.decodeFromJsonElement<Shield>(item)
            } else {
                json.decodeFromJsonElement<Armor>(item)
            }
        }

        4 -> json.decodeFromJsonElement<Accessories>(item)

        else -> when (item["ItemUICategory"]!!.jsonObject["ID"]!!.jsonPrimitive.int) {
            44, 46 -> json.decodeFromJsonElement<MedicineMeal>(item)

            else -> json.decodeFromJsonElement<GenericItem>(item)
        }
    }.let {
        it.createEmbed(
            it.createEmbedFields(language),
            lodestone
        )
    }
