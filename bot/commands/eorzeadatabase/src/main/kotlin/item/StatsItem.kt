package cloud.drakon.dynamisbot.eorzeadatabase.item

import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface StatsItem: Item {
    @SerialName("Stats") val stats: Map<String, Map<String, Int>>?

    suspend fun getStats(item: StatsItem, language: String) = coroutineScope {
        val stats = buildList {
            item.stats?.keys?.forEach {
                val key = Localisation.bonuses[it]?.getValue(language) ?: it

                val bonus = item.stats!!.getValue(it)
                val value = bonus.getValue("NQ")
                val valueHq = bonus["HQ"]

                if (valueHq != null) {
                    this.add(
                        "$key +$value / +$valueHq HQ"
                    )
                } else {
                    this.add("$key +$value")
                }
            }
        }

        stats.ifEmpty { null }
    }
}
