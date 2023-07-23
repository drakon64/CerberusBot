package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class MedicineMeal(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String?,
    @SerialName("IconHD") override val iconHd: String,
    @SerialName("ItemUICategory") override val itemUiCategory: Item.ItemUICategory,

    @SerialName("CanBeHq") val canBeHq: Int,
    @SerialName("Bonuses") val bonuses: Map<String, Bonus>? = null,
    @SerialName("LevelItem") val levelItem: String,
): Item {
    @Serializable class Bonus(
        @SerialName("Relative") val relative: Boolean,
        @SerialName("Value") val value: Short,
        @SerialName("Max") val max: Short,
        @SerialName("ValueHQ") val valueHq: Short,
        @SerialName("MaxHQ") val maxHq: Short
    )

    override suspend fun createEmbedFields(language: String) = coroutineScope {
        val bonusList = mutableListOf<String>()

        val embeds = mutableListOf(
            EmbedField(
                name = "Item Level",
                value = this@MedicineMeal.levelItem,
                inline = true
            )
        )

        if (bonuses != null) {
            for (i in bonuses.keys) {
                val key = Localisation.bonuses[i]?.getValue(language) ?: i
                val bonus = bonuses.getValue(i)

                if (bonus.relative) {
                    if (this@MedicineMeal.canBeHq == 1) {
                        bonusList.add(
                            "$key +${bonus.value}% (Max ${bonus.max}) / +${bonus.valueHq}% (Max ${bonus.maxHq}) <:hqlight:673889304359206923>"
                        )
                    } else {
                        bonusList.add(
                            "$key +${bonus.value}% (Max ${bonus.max})"
                        )
                    }
                }
            }

            embeds.add(
                EmbedField(
                    name = "Effects",
                    value = bonusList.joinToString("\n"),
                    inline = true
                )
            )
        }

        return@coroutineScope embeds.toTypedArray()
    }
}
