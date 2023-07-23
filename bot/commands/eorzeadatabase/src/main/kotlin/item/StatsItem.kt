package cloud.drakon.dynamisbot.eorzeadatabase.item

import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface StatsItem: Item {
    @SerialName("Stats") val stats: Map<String, Map<String, Int>>

    suspend fun getStats(item: StatsItem, language: String) = coroutineScope {
        val stats = mutableListOf<String>()

        for (i in item.stats.keys) {
            val key = Localisation.bonuses[i]?.getValue(language) ?: i

            val bonus = item.stats.getValue(i)
            val value = bonus.getValue("NQ")
            val valueHq = bonus["HQ"]

            if (valueHq != null) {
                stats.add(
                    "$key +$value / +$valueHq <:hqlight:673889304359206923>"
                )
            } else {
                stats.add(
                    "$key +$value"
                )
            }
        }

        return@coroutineScope stats.toList()
    }
}
