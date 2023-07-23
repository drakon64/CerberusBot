package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.cleanDescription
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface StatsItem: Item {
    @SerialName("Stats") val stats: Map<String, Map<String, Int>>

    @SerialName("ItemUICategory") val itemUiCategory: ItemUICategory

    @Serializable class ItemUICategory(@SerialName("Name") val name: String)

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

    override suspend fun createEmbed(fields: Array<EmbedField>?): Embed =
        coroutineScope {
            val description = if (this@StatsItem.description.isNullOrBlank()) {
                cleanDescription(this@StatsItem.itemUiCategory.name)
            } else {
                cleanDescription(
                    this@StatsItem.itemUiCategory.name
                    + "\n\n"
                    + this@StatsItem.description
                )
            }

            return@coroutineScope Embed(
                title = name,
                description = description,
                url = "https://eu.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                    name.replace(
                        " ", "+"
                    )
                }&db_search_category=item",
                thumbnail = EmbedThumbnail(url = "https://xivapi.com${iconHd}"),
                fields = fields
            )
        }
}
